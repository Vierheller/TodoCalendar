package de.vierheller.todocalendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoViewModel : ViewModel{
    @Inject
    lateinit var todoRepo :TodoRepository

    constructor(){

    }

    public fun addTodo(entity: TodoEntity){

    }

    public fun getTodos():LiveData<TodoEntity>{
        return MutableLiveData<TodoEntity>()
    }
}