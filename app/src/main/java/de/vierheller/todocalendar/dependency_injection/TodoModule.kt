package de.vierheller.todocalendar.dependency_injection

import android.app.Application
import dagger.Provides
import de.vierheller.todocalendar.TodoRepository
import javax.inject.Singleton

/**
 * Created by Vierheller on 01.11.2017.
 */
class TodoModule() {

    @Provides
    @Singleton
    public fun providesTodoRepository():TodoRepository{
        return TodoRepository();
    }
}