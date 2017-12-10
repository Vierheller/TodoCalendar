package de.vierheller.todocalendar.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import de.vierheller.todocalendar.TodoCalendarApplication
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

    init {
        TodoCalendarApplication.graph.inject(this)
    }

    fun init(intent:Intent){
        val taskId = intent.getLongExtra(TaskActivity.INTENT_ID, -1)
        if(taskId > -1){
            //Getting from DB
            todoRepo.getTodo(taskId){ task ->
                this.task.value = task
            }
        } else {
            //Initialize with default values
            val newTask = Task(taskName = "Is this freedom?", startDate = Calendar.getInstance().timeInMillis, durationMin = 30, priority = Priority.MEDIUM.level)
            this.task.setValue(newTask)
        }
    }

    fun save() {
        todoRepo.putTodo(task.value!!)
    }

    fun getTask():LiveData<Task>{
        return task
    }

    fun setStartDate(date: Date) {
        apply { task ->
            task.startDate = date.time
        }
    }

    fun setTaskName(text: String) {
        if(task.value!!.taskName != text)
            apply { task ->
                task.taskName = text
            }
    }

    fun setPriorityFromIndex(index: Int) {
        apply { task ->
            val selectedPrio = Priority.values()[index]
            task.priority = selectedPrio.level
        }
    }

    fun apply(job:(Task)->Unit){
        val oldValue = task.value!!
        job.invoke(oldValue)
        task.value = oldValue
    }
}
