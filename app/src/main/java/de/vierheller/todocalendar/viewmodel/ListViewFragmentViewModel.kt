package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.model.todo.TaskFilter

/**
 * Created by Vierheller on 19.12.2017.
 */
class ListViewFragmentViewModel: ViewModel() {
    lateinit var todoViewModel:TodoViewModel
    private var tasks: LiveData<List<Task>>? = null

    fun init(todoViewModel: TodoViewModel){
        this.todoViewModel = todoViewModel
    }

    fun getTasks(filter: TaskFilter): LiveData<List<Task>> {
        tasks = todoViewModel.getTasks(filter)
        return tasks!!
    }

    fun finishTask(position: Int):Boolean {
        val task = tasks?.value?.get(position)
        if(task!=null){
            todoViewModel.finishTask(task)
            return true
        }else{
            return false
        }
    }
}