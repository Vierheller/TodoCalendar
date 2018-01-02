package de.vierheller.todocalendar.repository

import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectRepository {
    fun getProjectsTree(callback:(tree:Tree<Project>)->Unit)  {
        doAsync {
            val list =  TodoCalendarApplication.database.projectDao().getAllProjects()
            val tree = Tree<Project>()
            tree.getRoot().children
                    .addAll(createTree(-1, tree.getRoot(), list as MutableList<Project>))

            uiThread {
                callback.invoke(tree)
            }
        }

    }

    /**
     * Creates a tree from the database projects
     */
    private fun createTree(parent:Long, nodeParent: Tree.Node<Project>, list:MutableList<Project>): List<Tree.Node<Project>> {
        val newChildren = mutableListOf<Tree.Node<Project>>()
        for(i in 0 until list.size){
            val project = list[i]
            if(project.parent == parent){
                val newNode = Tree.Node(project, nodeParent, mutableListOf<Tree.Node<Project>>())
                newChildren.add(newNode)
                list.remove(project)
                newNode.children.addAll(createTree(project.parent, newNode, list))
            }
        }
        return newChildren
    }
}
