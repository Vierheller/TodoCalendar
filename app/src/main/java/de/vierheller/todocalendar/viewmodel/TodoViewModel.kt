package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.repository.TodoRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoViewModel : ViewModel(){
    @Inject
    lateinit var todoRepo : TodoRepository

    private var tasks: LiveData<List<Task>>? = null
    private var onTasksUpdatedObservers:MutableList<OnTasksUpdatedListener> = ArrayList()

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun setOnTasksUpdatedListener(listener:OnTasksUpdatedListener){
        onTasksUpdatedObservers.add(listener)
    }

    fun removeOnTasksUpdatedListener(listener:OnTasksUpdatedListener){
        onTasksUpdatedObservers.remove(listener)
    }

    fun addTodo(entity: Task){
        todoRepo.putTodo(entity)
    }

    fun getTodo(id:Long, listener:((Task) -> Unit)) {
        doAsync {
            val task = todoRepo.getTodo(id);
            uiThread {
                listener.invoke(task)
            }
        }
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

    public interface OnTasksUpdatedListener{
        fun onWeekViewTasksUpdated() : ()->Unit
    }
}