package de.vierheller.todocalendar.model.todo

/**
 * Created by Vierheller on 19.12.2017.
 */
interface Filterable {
    fun filter(filer: TaskFilter): Boolean
}