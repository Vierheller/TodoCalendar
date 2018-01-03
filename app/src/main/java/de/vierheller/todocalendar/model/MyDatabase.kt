package de.vierheller.todocalendar.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.model.project.ProjectDao
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.model.todo.TodoDao

/**
 * Created by Vierheller on 02.11.2017.
 */
@Database(entities = arrayOf(Task::class, Project::class), version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun projectDao(): ProjectDao
}