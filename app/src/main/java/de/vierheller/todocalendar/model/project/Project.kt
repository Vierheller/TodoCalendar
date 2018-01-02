package de.vierheller.todocalendar.model.project

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Vierheller on 02.01.2018.
 */
@Entity(tableName = "projects")
data class Project(
        @PrimaryKey(autoGenerate = true) val uid: Long = 0,
        val name:String,
        val parent:Long
)
