package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import de.vierheller.todocalendar.model.TodoEntity

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoRepository(){
    public fun getAllTodos():LiveData<List<TodoEntity>>{
        val data = MutableLiveData<List<TodoEntity>>();
        data.value = listOf(TodoEntity())
        return data;
    }
}