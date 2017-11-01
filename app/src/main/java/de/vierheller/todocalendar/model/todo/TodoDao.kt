package de.vierheller.todocalendar.model.todo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Vierheller on 02.11.2017.
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Insert
    fun insert(todoEntity: TodoEntity)
}