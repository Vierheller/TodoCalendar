package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoRepository(){
    fun getTodosLive():LiveData<List<Task>>{
        return TodoCalendarApplication.database.todoDao().getAllTodosLive()
    }

    fun putTodo(entity: Task) {
        doAsync {
            TodoCalendarApplication.database.todoDao().insert(entity)
        }
    }

    fun getTodoLive(id:Long):LiveData<Task>{
        return TodoCalendarApplication.database.todoDao().getTodoLive(id)
    }


    fun getTodo(id:Long, listener:(Task)->Unit){
        doAsync {
            val task = TodoCalendarApplication.database.todoDao().getTodo(id)
            uiThread {
                listener.invoke(task)
            }
        }
    }

    fun getTodos():List<Task>{
        return TodoCalendarApplication.database.todoDao().getAllTodos()
    }


}
