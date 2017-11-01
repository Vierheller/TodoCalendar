package de.vierheller.todocalendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log

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