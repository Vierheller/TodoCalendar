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

    private var allTasks: LiveData<List<Task>>? = null

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun getTasks():LiveData<List<Task>>{
        if(allTasks == null){
            allTasks = MutableLiveData<List<Task>>()
            loadTasks()
        }
        return allTasks!!
    }

    private fun loadTasks() {
        allTasks = todoRepo.getTodosLive()
    }

    fun finishTask(task:Task){
        todoRepo.finishTask(task)
    }

    interface OnTasksUpdatedListener{
        fun onWeekViewTasksUpdated() : ()->Unit
    }
}