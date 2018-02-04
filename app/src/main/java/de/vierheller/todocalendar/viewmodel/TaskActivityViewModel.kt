package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.extensions.apply
import de.vierheller.todocalendar.model.todo.Priority
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.repository.TodoRepository
import de.vierheller.todocalendar.view.TaskActivity
import java.util.*
import javax.inject.Inject

/**
 * Created by Vierheller on 10.12.2017.
 */
class TaskActivityViewModel : ViewModel(){
    @Inject
    lateinit var todoRepo : TodoRepository

    private var task: MutableLiveData<Task> = MutableLiveData()

    //Variable to find out if the current Task is new or already SAVED in DB
    private var isSaved:Boolean = false

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun init(intent:Intent){
        val taskId = intent.getLongExtra(TaskActivity.INTENT_ID, -1)
        isSaved = taskId > -1
        if(isSaved){
            //Getting from DB
            todoRepo.getTodo(taskId){ task ->
                this.task.value = task
            }
        } else {
            //Initialize with default values
            val newTask = Task(taskName = "", startDate = Calendar.getInstance().timeInMillis, durationMin = 30, priority = Priority.MEDIUM.level)
            this.task.setValue(newTask)
        }
    }

    fun save() {
        todoRepo.putTodo(task.value!!)
    }

    fun deleteTask() {
        todoRepo.removeTask(this.task.value!!)
    }

    fun isTaskSaved(): Boolean {
        return isSaved
    }

    fun getTask():LiveData<Task>{
        return task
    }

    fun setStartDate(date: Date) {
        task.apply { task ->
            task.startDate = date.time
        }
    }

    fun setTaskName(text: String) {
        if(task.value?.taskName != text)
            task.apply { task ->
                task.taskName = text
            }
    }

    fun setPriorityFromIndex(index: Int) {
        task.apply { task ->
            val selectedPrio = Priority.values()[index]
            task.priority = selectedPrio.level
        }
    }

    fun setDuration(new: Int) {
        task.apply { task ->
            task.durationMin = new
        }
    }

    fun setBuffer(new: Int) {
        task.apply { task ->
            task.bufferTime = new
        }
    }

    fun setNote(new: String) {
        task.apply { task ->
            task.note = new
        }
    }
}


