package de.vierheller.todocalendar.model.todo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by Vierheller on 02.11.2017.
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM tasks WHERE uid = :id")
    fun getTodoLive(id:Long):LiveData<Task>

    @Query("SELECT * FROM tasks WHERE uid = :id")
    fun getTodo(id:Long):Task


    @Query("SELECT * FROM tasks")
    fun getAllTodosLive(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTodos():List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: Task)
}