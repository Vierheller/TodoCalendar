package de.vierheller.todocalendar.model.project

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query
import de.vierheller.todocalendar.model.todo.Task

/**
 * Created by Vierheller on 02.01.2018.
 */
interface ProjectDAO {
    @Query("SELECT * FROM projects")
    fun getAllProjectsLive(): LiveData<List<Project>>
}