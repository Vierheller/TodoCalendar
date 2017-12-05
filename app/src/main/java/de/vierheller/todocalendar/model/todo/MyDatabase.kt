package de.vierheller.todocalendar.model.todo

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by Vierheller on 02.11.2017.
 */
@Database(entities = arrayOf(Task::class), version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}