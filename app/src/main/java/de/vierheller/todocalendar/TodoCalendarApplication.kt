package de.vierheller.todocalendar

import android.app.Application
import android.arch.persistence.room.Room
import de.vierheller.todocalendar.dependency_injection.ApplicationComponent
import de.vierheller.todocalendar.dependency_injection.DaggerApplicationComponent
import de.vierheller.todocalendar.dependency_injection.TodoModule
import de.vierheller.todocalendar.model.todo.MyDatabase

/**
 * Created by Vierheller on 01.11.2017.
 */
class TodoCalendarApplication : Application() {
    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
        @JvmStatic lateinit var database: MyDatabase
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().todoModule(TodoModule()).build()
        graph.inject(this)
        database =  Room.databaseBuilder(this, MyDatabase::class.java, "todo-calendar-db").build()
    }
}