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
           val parent_todo_id: Long = 0,
           val taskName: String,
           val startDate: Long,
           val durationMin: Int,
           val bufferTime: Int = 0,
           val priority: Int,
           val note:String = ""){

    override fun toString(): String {
        return "Task(uid=$uid, parent_todo_id=$parent_todo_id, name='$taskName', duration_min=$durationMin, buffer_time=$bufferTime, priority=$priority, note='$note')"
    }



    fun getStartTime(): Calendar {
        var cal: Calendar = Calendar.getInstance();
        cal.timeInMillis = startDate
        return cal;
    }

    fun getEndTime(): Calendar {
        var endDate:Calendar = Calendar.getInstance();
        endDate.timeInMillis = startDate
        endDate.add(Calendar.MINUTE, durationMin + bufferTime);
        return endDate;
    }



}