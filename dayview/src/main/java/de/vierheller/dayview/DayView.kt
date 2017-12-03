package de.vierheller.dayview

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by Vierheller on 02.12.2017.
 */

class DayView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var events: List<IEvent>? = null;


    fun setEvents(events:List<IEvent>){
        this.events = events;
    }

    fun clearEvents(){
        this.events = null;
    }


}