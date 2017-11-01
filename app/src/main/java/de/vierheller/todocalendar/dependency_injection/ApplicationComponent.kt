package de.vierheller.todocalendar.dependency_injection

import dagger.Component
import de.vierheller.todocalendar.MainActivity
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.TodoViewModel
import javax.inject.Singleton

/**
 * Created by Vierheller on 01.11.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, TodoModule::class))
interface ApplicationComponent {
    fun inject(applicationComponent: TodoCalendarApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(viewModel: TodoViewModel)
}