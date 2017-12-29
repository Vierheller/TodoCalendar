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
import java.util.*

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
        val array = tasks!!.value!!
        val sections = mutableListOf<SimpleSectionedRecyclerViewAdapter.Section>()
        val ranges = DateRangesCalculator.SectionDateRanges.values()

        for(i in 0 until ranges.size){
            val index = indexForRange(DateRangesCalculator.SectionDateRanges.values()[i], array)
            if(index > -1){
                sections.add(SimpleSectionedRecyclerViewAdapter.Section(index, "\"${ranges[i].name}\""))
            }
        }
        return sections
    }

    private fun indexForRange(range:DateRangesCalculator.SectionDateRanges, array:List<Task>):Int{
        for(i in 0 until array.size){
            if(range.calculator.calculate(array[i].getStartTime())){
                return i
            }
        }
        return -1
    }

    private class DateRangesCalculator(val calculator:(Calendar)->Boolean){
        enum class SectionDateRanges(val calculator:DateRangesCalculator) {
            LAST_YEAR(DateRangesCalculator(lastYearCalculator)),
            LAST_SIX_MONTHS(DateRangesCalculator(lastSixMonthsCalculator)),
            LAST_MONTH(DateRangesCalculator(lastSixMonthsCalculator)),
            LAST_WEEK(DateRangesCalculator(lastSixMonthsCalculator)),
            YESTERDAY(DateRangesCalculator(lastSixMonthsCalculator)),
            TODAY(DateRangesCalculator(lastSixMonthsCalculator)),
            TOMORROW(DateRangesCalculator(lastSixMonthsCalculator)),
            NEXT_WEEK(DateRangesCalculator(lastSixMonthsCalculator)),
            FUTURE(DateRangesCalculator(lastSixMonthsCalculator));
        }


        fun calculate(date:Calendar):Boolean = calculator.invoke(date)

        companion object{
            private val lastYearCalculator: (Calendar) -> Boolean = {
                val comparator = Calendar.getInstance()
                comparator.add(Calendar.YEAR, -1)
                it dayIsBefore comparator
            }

            private val lastSixMonthsCalculator: (Calendar) -> Boolean = {
                false
            }

            //TODO needs to be replaced with compare of days and not exact times
            private infix fun Calendar.dayIsBefore(it: Calendar): Boolean {
                return this.timeInMillis < it.timeInMillis
            }

            //TODO needs to be replaced with compare of days and not exact times
            private infix fun Calendar.dayIsAfter(it: Calendar): Boolean {
                return this.timeInMillis > it.timeInMillis
            }

            private fun Calendar.dayInBetween(before: Calendar, after:Calendar): Boolean {
                return (this dayIsAfter before) && (this dayIsBefore after)
            }
        }
    }



}




