package de.vierheller.todocalendar.dependency_injection

import dagger.Module
import dagger.Provides
import de.vierheller.todocalendar.repository.ProjectRepository
import de.vierheller.todocalendar.repository.TodoRepository
import javax.inject.Singleton

/**
 * Created by Vierheller on 03.01.2018.
 */
@Module
class ProjectModule {
    @Provides
    @Singleton
    fun providesProjectRepository(): ProjectRepository {
        return ProjectRepository()
    }
}