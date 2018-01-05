package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectRepository {
    private var treeLiveData = MediatorLiveData<Tree<Project>>()
    private var projectsLiveData: LiveData<List<Project>>? = null

    fun getModelProjectTree():LiveData<Tree<Project>>  {
        if(projectsLiveData == null){
            projectsLiveData = getProjectsLive()

            treeLiveData.addSource(projectsLiveData!!){ list ->
                transformList(list!!)
            }
        }
        return treeLiveData
    }

    fun transformList(list:List<Project>): Tree<Project> {
        val tree = Tree<Project>()
        tree.getRoot().children
                .addAll(createTree(-1, tree.getRoot(), list))
        return tree
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
    private fun createTree(parent:Long, nodeParent: Tree.Node<Project>, list:List<Project>): List<Tree.Node<Project>> {
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
