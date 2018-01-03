package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.repository.ProjectRepository
import de.vierheller.todocalendar.repository.TodoRepository
import javax.inject.Inject

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectsPragmentViewModel : ViewModel() {
    @Inject
    lateinit var projectRepo : ProjectRepository

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun getTree(){
        projectRepo.getProjectsTree {
            Log.d("TAG", it.toString())
        }
    }
}