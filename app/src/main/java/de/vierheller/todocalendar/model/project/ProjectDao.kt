package de.vierheller.todocalendar.model.project

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/**
 * Created by Vierheller on 02.01.2018.
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): List<Project>

    @Query("SELECT * FROM projects")
    fun getAllProjectsLive(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    @Query("DELETE FROM projects")
    fun deleteTable()
}