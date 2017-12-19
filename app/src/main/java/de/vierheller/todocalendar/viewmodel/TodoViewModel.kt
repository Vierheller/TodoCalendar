package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.repository.TodoRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.FieldPosition
import javax.inject.Inject

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoViewModel : ViewModel(){
    @Inject
    lateinit var todoRepo : TodoRepository

    private var tasks: LiveData<List<Task>>? = null

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun getTasks():LiveData<List<Task>>{
        if(tasks==null){
            tasks = MutableLiveData<List<Task>>()
            loadTasks()
        }
        return tasks!!
    }

    private fun loadTasks() {
        tasks = todoRepo.getTodosLive()
    }

    fun finishTask(position: Int):Boolean{
        val task = tasks?.value?.get(position)
        if(task!=null) {
            todoRepo.finishTask(task)
            return true
        }else{
            return false
        }
    }

    interface OnTasksUpdatedListener{
        fun onWeekViewTasksUpdated() : ()->Unit
    }
}