package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.unnamed.b.atv.model.TreeNode
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree
import de.vierheller.todocalendar.repository.ProjectRepository
import de.vierheller.todocalendar.view.dialogs.SpinnerItem
import de.vierheller.todocalendar.view.main.projects.ProjectItem
import javax.inject.Inject

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectsFragmentViewModel : ViewModel() {
    @Inject
    lateinit var projectRepo : ProjectRepository

    private var liveTree: LiveData<Tree<Project>>? = null
    private var liveViewTree = MediatorLiveData<TreeNode>()

    private var projects: LiveData<List<Project>>? = null



    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun getTree():LiveData<TreeNode>{
        if(liveTree==null){
            liveTree = projectRepo.getModelProjectTree()
            Log.d("TAG", "getTree()" )
            liveViewTree.addSource(liveTree!!){ tree ->
                Log.d("TAG", "transformModelTreeToViewTree() ${tree}" )
                liveViewTree.value = transformModelTreeToViewTree(tree!!)
            }
        }

        return liveViewTree
    }

    fun getProjects(): LiveData<List<Project>> {
        if(this.projects == null){
            this.projects = projectRepo.getProjectsLive()
        }
        return this.projects!!
    }

    fun transformModelTreeToViewTree(tree:Tree<Project>): TreeNode? {
        Log.d("TAG", tree.toString())
        val treeRoot = TreeNode.root()
        treeRoot.addChildren(createTree(tree!!.getRoot(), 0))
        return treeRoot
    }

    /**
     * Creates a Tree with the TreeNode class based on the projects model tree.
     *
     * Call this method with
     *  viewNode = root of ViewTree
     *  modelNode = root of Model Tree
     *  level = 0
     */
    private fun createTree(modelNode: Tree.Node<Project>, level:Int): List<TreeNode> {
        val newViewChildren = mutableListOf<TreeNode>()

        for (modelNodeChild in modelNode.children){
            val modelNode = modelNodeChild.data!!
            val newViewNode = TreeNode(ProjectItem(modelNode.uid, modelNode.name, modelNode.parent, level))
            newViewNode.addChildren(createTree(modelNodeChild, level+1))
            newViewChildren.add(newViewNode)
        }

        return newViewChildren
    }

    fun insertOrUpdateProject(id:Long, name: String, parentId: Long) {
        projectRepo.insertProject(Project(uid = id, name = name, parent = parentId))
    }

    fun projectPositionFromParentId(parentId:Long, observer:Observer<Int>){
        projectRepo.getProjects(Observer { projects->
            for(i in 0 until projects!!.size){
                val project = projects[i]
                if(project.parent == parentId){
                    observer.onChanged(i)
                    return@Observer
                }
            }
        })
    }

    /**
     * Creates new LiveData for the provided id.
     * Reason is that the current project should not be selectable.
     * If it would be selectable, a object would be parent of itself
     * and disappear from tree.
     */
    fun getParentsSpinnerList(curId:Long): LiveData<List<SpinnerItem>> {
        //Null check -> init projects ifnot already happened
        if(this.projects == null){
            getProjects()
        }

        val parentsSpinnerList = MediatorLiveData<List<SpinnerItem>>()
        parentsSpinnerList.addSource(this.projects!!, Observer {
            val list = mutableListOf<SpinnerItem>()

            //TODO translationable name for root node
            list.add(SpinnerItem("None", null))

            //Filtering -> do not add current project
            it!!.forEach {
                if(shouldAddToParentList(it))
                    list.add(SpinnerItem(it.name, it))
            }

            parentsSpinnerList.value = list
        })
        return parentsSpinnerList
    }

    private fun shouldAddToParentList(project:Project, curId:Long):Boolean{
        when{
            project.uid == curId ->{
                return false
            }
            project.isParentOf(curId)
        }
    }
}