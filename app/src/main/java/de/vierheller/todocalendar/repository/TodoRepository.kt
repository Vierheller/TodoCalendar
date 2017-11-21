package de.vierheller.todocalendar.repository

import android.arch.lifecycle.LiveData
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task

/**
 * Created by Vierheller on 01.11.2017.
 */

class TodoRepository(){
    public fun getTodos():LiveData<List<Task>>{
        return TodoCalendarApplication.database.TodoDao().getAllTodos();
    }

    fun putTodo(entity: Task) {
        TodoCalendarApplication.database.TodoDao().insert(entity)
    }


}
