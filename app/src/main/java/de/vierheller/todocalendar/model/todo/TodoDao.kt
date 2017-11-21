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
    @Query("SELECT * FROM tasks")
    fun getAllTodos(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: Task)
}