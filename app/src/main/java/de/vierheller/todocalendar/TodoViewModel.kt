package de.vierheller.todocalendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import de.vierheller.todocalendar.dependency_injection.ApplicationComponent
import javax.inject.Inject

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoViewModel : ViewModel(){
    @Inject
    lateinit var todoRepo :TodoRepository

    var todos : LiveData<List<TodoEntity>> = MutableLiveData()

    public fun addTodo(entity: TodoEntity){

    }


    fun init(){
        TodoCalendarApplication.graph.inject(this);

        todos = todoRepo.getAllTodos()
    }
}