package de.vierheller.todocalendar

import android.app.Application
import de.vierheller.todocalendar.dependency_injection.AppModule
import de.vierheller.todocalendar.dependency_injection.ApplicationComponent

/**
 * Created by Vierheller on 01.11.2017.
 */
class TodoCalendarApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}