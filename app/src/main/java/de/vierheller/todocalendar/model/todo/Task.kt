package de.vierheller.todocalendar.model.todo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Vierheller on 01.11.2017.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val parent_todo_id: Long = 0,
    val name: String,
    val duration_min: Int,
    val buffer_time: Int,
    val priority: Int,
    val note:String = ""
)