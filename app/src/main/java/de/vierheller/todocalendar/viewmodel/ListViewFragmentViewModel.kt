package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Priority
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.model.todo.TaskFilter
import de.vierheller.todocalendar.model.todo.TaskSorting
import de.vierheller.todocalendar.view.main.list.extra.SimpleSectionedRecyclerViewAdapter

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
        if(tasks == null){
            loadtasks()
        }

        return tasks!!
    }

    fun loadtasks(){
        tasks = MediatorLiveData<List<Task>>()
        addTodoSource();
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

    fun getSectionsForCurrentSorting(): List<SimpleSectionedRecyclerViewAdapter.Section> {
        when(currentSorting){
            TaskSorting.PRIORITY->{
                return getSelectionForPriority()
            }
            TaskSorting.DATE -> {
                return getSectionForDate()
            }
            TaskSorting.NAME -> {
                return listOf()
            }
            else ->{
                return listOf()
            }
        }
    }

    private fun getSelectionForPriority(): List<SimpleSectionedRecyclerViewAdapter.Section> {
        val array = tasks!!.value!!
        val sections = mutableListOf<SimpleSectionedRecyclerViewAdapter.Section>()
        val priorities = Priority.values()

        for (i in 0 until priorities.size){
            val index = findNextIndexForPrio(priorities[i], array)
            sections.add(SimpleSectionedRecyclerViewAdapter.Section(index, "Priority: \"${priorities[i].name}\""))
        }

        return sections
    }

    /**
     * Premise: Array been sorted by priorities
     * Return: Index where a new section should be placed
     */
    private fun findNextIndexForPrio(searchedPrio:Priority, array:List<Task>):Int{
        for (j in 0 until array.size){
            val curPrio = array[j].priority
            if(curPrio >= searchedPrio.level){
                return j
            }
        }
        return array.size - 1
    }

    /**
     * Premise: Tasks sorted by Date
     * Return: Sections like:
     *      last Year
     *      last 6 Months
     *      last Month
     *      last Week
     *      yesterday
     *      today
     *      tomorrow
     *      (next week)
     *      future
     */
    private fun getSectionForDate(): List<SimpleSectionedRecyclerViewAdapter.Section> {
        return listOf()
    }
}