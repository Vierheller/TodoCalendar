package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.Tree
import org.jetbrains.anko.doAsync

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectRepository {
    private var treeLiveData = MediatorLiveData<Tree<Project>>()
    private var projectsLiveData: LiveData<List<Project>>? = null

    fun getModelProjectTree():LiveData<Tree<Project>>  {
        if(projectsLiveData == null){
            projectsLiveData = getProjectsLive()
            Log.d("TAG", "getModelProjectTree()")
            treeLiveData.addSource(projectsLiveData!!){ list ->
                Log.d("TAG", "transformListToModelTree() ${list}")
                treeLiveData.value = transformListToModelTree(list!!)
            }
        }
        return treeLiveData
    }

    fun transformListToModelTree(list:List<Project>): Tree<Project> {
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

    fun addProject(project:Project){
        doAsync {
            Log.d("TAG", "Add Project")
            TodoCalendarApplication.database.projectDao().addProject(project)
        }
    }

    fun getProjectsLive(): LiveData<List<Project>> {
        return TodoCalendarApplication.database.projectDao().getAllProjectsLive()
    }

    fun getProjects(observer: Observer<List<Project>>){
        doAsync {
            observer.onChanged(TodoCalendarApplication.database.projectDao().getAllProjects())
        }
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
