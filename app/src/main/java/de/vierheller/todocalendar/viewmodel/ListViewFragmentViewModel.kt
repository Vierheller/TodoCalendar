package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.model.todo.TaskFilter
import de.vierheller.todocalendar.model.todo.TaskSorting

/**
 * Created by Vierheller on 19.12.2017.
 */
class ListViewFragmentViewModel: ViewModel() {
    lateinit var todoViewModel:TodoViewModel
    private var tasks: MediatorLiveData<List<Task>>? = null
    private var currentFilter: TaskFilter? = null
    private var currentSorting: TaskSorting? = null

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
            var list = filterList(sourceTasks!!)
            list = sortList(list)
            tasks!!.value = list
        }
    }

    private fun sortList(list: List<Task>): List<Task> {
        when(currentSorting){
            TaskSorting.DATE->{
                return list.sortedBy { it.startDate }
            }
            TaskSorting.PRIORITY->{
                return list.sortedBy { it.priority }
            }
            TaskSorting.NAME -> {
                return list.sortedBy { it.taskName }
            }
            else->{
                return list.sortedBy { it.startDate }
            }
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

    fun setSorting(sorting:TaskSorting){
        currentSorting = sorting
        resetSources()
    }

    fun menuItemSelected(item: Int): Boolean {
        when(item){
            //Filters
            R.id.action_filter_finished->{
                setFilter(TaskFilter.FINISHED)
                return true
            }
            R.id.action_filter_unfinished->{
                setFilter(TaskFilter.UNFINISHED)
                return true
            }
            R.id.action_filter_none->{
                setFilter(TaskFilter.NONE)
                return true
            }
            //Sorting
            R.id.action_sort_date->{
                setSorting(TaskSorting.DATE)
                return true
            }
            R.id.action_sort_prio->{
                setSorting(TaskSorting.PRIORITY)
                return true
            }
            R.id.action_sort_name->{
                setSorting(TaskSorting.NAME)
                return true
            }
        }
        return false
    }
}