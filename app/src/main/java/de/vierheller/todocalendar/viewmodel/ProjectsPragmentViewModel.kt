package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
class ProjectsPragmentViewModel : ViewModel() {
    @Inject
    lateinit var projectRepo : ProjectRepository

    private var liveTree: MutableLiveData<TreeNode>? = null;

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun getTree():LiveData<TreeNode>{
        if(liveTree==null){
            liveTree = MutableLiveData()
            projectRepo.getProjectsTree()
            {
                Log.d("TAG", it.toString())
                val treeRoot = TreeNode.root()
                treeRoot.addChildren(createTree(it.getRoot(), 0))
                liveTree!!.value = treeRoot
            }
        }

        return liveTree!!

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