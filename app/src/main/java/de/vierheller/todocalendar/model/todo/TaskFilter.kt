package de.vierheller.todocalendar.model.todo

import android.content.Context
import de.vierheller.todocalendar.R

/**
 * Created by Vierheller on 19.12.2017.
 */
enum class TaskFilter(val stringRes:Int) {
    NONE(R.string.task_filter_all),
    UNFINISHED(R.string.task_filter_unfinished),
    FINISHED(R.string.task_filter_finished);
    companion object {
        fun asStringArray(context: Context):Array<String>{
            return TaskFilter.values().map { filter ->
                context.getString(filter.stringRes)
            }.toTypedArray()
        }
    }
}