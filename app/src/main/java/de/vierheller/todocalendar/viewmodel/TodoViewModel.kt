package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alamkanak.weekview.WeekViewEvent
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

    private var tasks: LiveData<List<Task>>? = null

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    public fun addTodo(entity: Task){
        todoRepo.putTodo(entity)
    }

    fun getTasks():LiveData<List<Task>>{
        if(tasks==null){
            tasks = MutableLiveData<List<Task>>()
            loadTasks()
        }
        return tasks!!
    }

    fun getTasksAsWeekViewEvent():MutableList<WeekViewEvent>{
        val todos = todoRepo.getTodos();

        return MutableList<WeekViewEvent>(todos.size){index ->
            val todo = todos.get(index)
            WeekViewEvent(todo.uid, todo.name, todo.getStartingDate(), todo.getEndDate());
        };
    }

    private fun loadTasks() {
        tasks = todoRepo.getTodosLive();
    }
}