package de.vierheller.todocalendar.model.todo

import android.content.Context
import de.vierheller.todocalendar.R

/**
 * Created by Vierheller on 05.12.2017.
 */
enum class Priority(val level: Int, val stringRes:Int) {
    LOW(1, R.string.priority_low),
    MEDIUM(2, R.string.priority_medium),
    HIGH(3, R.string.priority_high);



    companion object {
        fun fromLevel(level: Int): Priority? {
            when(level){
                LOW.level -> return Priority.LOW
                MEDIUM.level -> return Priority.MEDIUM
                HIGH.level -> return Priority.HIGH
            }
            return null
        }

        fun asStringArray(context: Context):Array<String>{
            return Priority.values().map { priority ->
                context.getString(priority.stringRes)
            }.toTypedArray()
        }
    }
}


