package de.vierheller.todocalendar

import android.app.Application
import de.vierheller.todocalendar.dependency_injection.AppModule
import de.vierheller.todocalendar.dependency_injection.ApplicationComponent
import de.vierheller.todocalendar.dependency_injection.DaggerApplicationComponent
import de.vierheller.todocalendar.dependency_injection.TodoModule

/**
 * Created by Vierheller on 01.11.2017.
 */
class TodoCalendarApplication : Application() {
    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().todoModule(TodoModule()).build();
        graph.inject(this)
    }
}