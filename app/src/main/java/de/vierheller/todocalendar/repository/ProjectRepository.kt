package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectRepository {
    var treeLiveData = MutableLiveData<Tree<Project>>()
    fun getProjectsTree()  {
        getProjectsLive().observeForever{
            val tree = Tree<Project>()
            tree.getRoot().children
                    .addAll(createTree(-1, tree.getRoot(), it!!.toMutableList()))

            treeLiveData.value
        }
    }

    fun getProjectsTest(): List<Project>{
        return listOf(
                Project(0,"Project1", -1),
                Project(1,"Project1-1", 0),
                Project(2,"Project1-2", 0),
                Project(3,"Project2", -1),
                Project(4,"Project3", -1),
                Project(5,"Project3-1", 4),
                Project(6,"Project3-1-1", 5),
                Project(7,"Project4", -1),
                Project(8,"Project5", -1)
        )
    }

    fun getProjectsLive(): LiveData<List<Project>> {
        return TodoCalendarApplication.database.projectDao().getAllProjectsLive()
    }

    fun getProjects(): List<Project> {
        return  TodoCalendarApplication.database.projectDao().getAllProjects()
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
//                list.remove(project)
                newNode.children.addAll(createTree(project.uid, newNode, list))
            }
        }
        return newChildren
    }
}
