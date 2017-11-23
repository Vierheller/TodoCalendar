package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import org.jetbrains.anko.doAsync

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoRepository(){
    public fun getTodosLive():LiveData<List<Task>>{
        return TodoCalendarApplication.database.TodoDao().getAllTodosLive();
    }

    fun putTodo(entity: Task) {
        doAsync {
            TodoCalendarApplication.database.TodoDao().insert(entity)
        }
    }

    fun getTodos():List<Task>{
        return TodoCalendarApplication.database.TodoDao().getAllTodos();
    }


}
