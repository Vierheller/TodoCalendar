package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
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
            liveViewTree.addSource(liveTree!!){ tree ->
                transformModelTreeToViewTree(tree!!)
            }
        }

        return liveViewTree
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
            val newViewNode = TreeNode(ProjectItem(modelNodeChild.data?.name?:"Unknown",level))
            newViewNode.addChildren(createTree(modelNodeChild, level+1))
            newViewChildren.add(newViewNode)
        }

        return newViewChildren
    }
}