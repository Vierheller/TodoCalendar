package de.vierheller.todocalendar.dependency_injection

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Vierheller on 01.11.2017.
 */
@Module
class AppModule (private val application: Application){

    /**
     * Allow the application context to be injected but require that it be annotated with ... to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    fun provideApplication(): Application{
      return application
    }
}