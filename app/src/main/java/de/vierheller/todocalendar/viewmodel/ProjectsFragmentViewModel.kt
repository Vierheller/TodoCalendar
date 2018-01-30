package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.unnamed.b.atv.model.TreeNode
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree
import de.vierheller.todocalendar.repository.ProjectRepository
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
        return projectRepo.getProjectsLive()
    }

    fun transformModelTreeToViewTree(tree:Tree<Project>): TreeNode? {
        Log.d("TAG", tree.toString())
        val treeRoot = TreeNode.root()
        treeRoot.addChildren(createTree(tree.getRoot(), 0))
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
            val newViewNode = TreeNode(ProjectItem(modelNodeChild.data?.name?:"Unknown",level))
            newViewNode.addChildren(createTree(modelNodeChild, level+1))
            newViewChildren.add(newViewNode)
        }

        return newViewChildren
    }

    fun addProject(name: String, parent: Int) {
        if(parent != -1){
            projectRepo.getProjects(Observer {
                val parentProject = it!!.get(parent)
                Log.d("TAG", "Parent is ${parentProject}")
                projectRepo.addProject(Project(name = name, parent = parentProject.uid))
            })
        }else{
            projectRepo.addProject(Project(name = name, parent = -1))
        }
    }
}