package de.vierheller.todocalendar.dependency_injection

import dagger.Component
import de.vierheller.todocalendar.view.main.MainActivity
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.viewmodel.ProjectsFragmentViewModel
import de.vierheller.todocalendar.viewmodel.TaskActivityViewModel
import de.vierheller.todocalendar.viewmodel.TodoViewModel
import javax.inject.Singleton

/**
 * Created by Vierheller on 01.11.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, TodoModule::class, ProjectModule::class))
interface ApplicationComponent {
    fun inject(applicationComponent: TodoCalendarApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(viewModel: TodoViewModel)

    fun inject(viewModel: TaskActivityViewModel)

    fun inject(viewMode: ProjectsFragmentViewModel)
}