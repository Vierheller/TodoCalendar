package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.*
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.repository.TodoRepository
import de.vierheller.todocalendar.model.todo.TaskFilter
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

    fun getTasks(filter: TaskFilter):LiveData<List<Task>>{
        if(allTasks ==null){
            allTasks = MediatorLiveData<List<Task>>()
            loadTasks(filter)
        }
        return filterLiveData(filter)
    }

    private fun filterLiveData(filter:TaskFilter): MediatorLiveData<List<Task>> {
        val filteredList = MediatorLiveData<List<Task>>()
        filteredList.addSource(todoRepo.getTodosLive()){ sourceTasks ->
            val list = sourceTasks!!.filter{
                it.filter(filter)
            }
            filteredList.value = list
        }
        return filteredList
    }

    private fun loadTasks(filter: TaskFilter) {
        allTasks = todoRepo.getTodosLive()
    }

    fun finishTask(task:Task){
        todoRepo.finishTask(task)
    }

    interface OnTasksUpdatedListener{
        fun onWeekViewTasksUpdated() : ()->Unit
    }
}