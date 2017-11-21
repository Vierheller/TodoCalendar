package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.repository.TodoRepository
import javax.inject.Inject

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoViewModel : ViewModel(){
    @Inject
    lateinit var todoRepo : TodoRepository

    var todos : LiveData<List<Task>> = MutableLiveData()

    public fun addTodo(entity: Task){
        todoRepo.putTodo(entity)
    }


    fun init(){
        TodoCalendarApplication.graph.inject(this);

        todos = todoRepo.getAllTodos()
    }
}