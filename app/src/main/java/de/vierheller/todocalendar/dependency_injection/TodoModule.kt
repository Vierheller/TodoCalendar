package de.vierheller.todocalendar.dependency_injection

import dagger.Module
import dagger.Provides
import de.vierheller.todocalendar.repository.TodoRepository
import javax.inject.Singleton

/**
 * Created by Vierheller on 01.11.2017.
 */
@Module
class TodoModule() {

    @Provides
    @Singleton
    public fun providesTodoRepository(): TodoRepository {
        return TodoRepository();
    }
}