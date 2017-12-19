package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.model.todo.TaskFilter

/**
 * Created by Vierheller on 19.12.2017.
 */
class ListViewFragmentViewModel: ViewModel() {
    lateinit var todoViewModel:TodoViewModel
    private var tasks: MediatorLiveData<List<Task>>? = null
    private var currentFilter: TaskFilter? = null

    fun init(todoViewModel: TodoViewModel){
        this.todoViewModel = todoViewModel
    }

    fun getTasks(): LiveData<List<Task>> {
        tasks = MediatorLiveData<List<Task>>()

        addTodoSource();

        return tasks!!
    }

    private fun addTodoSource(){
        //Filtering the LiveData
        tasks!!.addSource(todoViewModel.getTasks()){ sourceTasks ->
            val list = filterList(sourceTasks!!)
            tasks!!.value = list
        }
    }

    private fun removeTodoSource(){
        tasks!!.removeSource(todoViewModel.getTasks())
    }

    private fun resetSources(){
        removeTodoSource()
        addTodoSource()
    }

    private fun filterList(list:List<Task>):List<Task>{
        return list.filter {
            it.filter(currentFilter?:TaskFilter.UNFINISHED)
        }
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

    fun setFilter(filter:TaskFilter) {
        currentFilter = filter
        resetSources()
    }
}