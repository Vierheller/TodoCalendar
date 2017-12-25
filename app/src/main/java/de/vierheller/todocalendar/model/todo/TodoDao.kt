package de.vierheller.todocalendar.model.todo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

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

    @Update
    fun update(todo:Task)

    @Delete
    fun delete(todo:Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: Task)
}