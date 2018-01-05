package de.vierheller.todocalendar.model.project

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Vierheller on 02.01.2018.
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): List<Project>

    @Query("SELECT * FROM projects")
    fun getAllProjectsLive(): LiveData<List<Project>>

    @Insert
    fun addProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    @Query("DELETE FROM projects")
    fun deleteTable()
}