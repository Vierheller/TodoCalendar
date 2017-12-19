package de.vierheller.todocalendar.model.todo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Vierheller on 01.11.2017.
 */
@Entity(tableName = "tasks")
class Task(@PrimaryKey(autoGenerate = true)
           val uid: Long = 0,
           var parent_todo_id: Long = 0,
           var taskName: String,
           var startDate: Long,
           var durationMin: Int,
           var bufferTime: Int = 0,
           var priority: Int,
           var note:String = "",
           var finished:Boolean = false){

    override fun toString(): String {
        return "Task(uid=$uid, parent_todo_id=$parent_todo_id, name='$taskName', duration_min=$durationMin, buffer_time=$bufferTime, priority=$priority, note='$note', finished='$finished')"
    }

    fun getStartTime(): Calendar {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = startDate
        return cal
    }

    fun getEndTime(): Calendar {
        val endDate:Calendar = Calendar.getInstance()
        endDate.timeInMillis = startDate
        endDate.add(Calendar.MINUTE, durationMin + bufferTime)
        return endDate
    }
}