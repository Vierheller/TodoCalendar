package de.vierheller.dayview

import java.util.*

/**
 * Created by Vierheller on 02.12.2017.
 */
interface IEvent {
    fun getName():String

    fun getStartTime():Calendar

    fun getEndTime():Calendar
}