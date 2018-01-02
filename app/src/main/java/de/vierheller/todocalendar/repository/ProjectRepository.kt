package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.project.Project

/**
 * Created by Vierheller on 02.01.2018.
 */
class ProjectRepository {
    fun getProjectsTree(){
        val list =  TodoCalendarApplication.database.projectDao().getAllProjectsLive()
    }
}