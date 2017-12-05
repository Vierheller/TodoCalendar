package de.vierheller.dayview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Vierheller on 02.12.2017.
 */

class DayView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //Layout Attribute Defaults
    private val HOUR_HEIGHT_DEF = 50

    //Layout Attributes
    private var hourHeight:Int = HOUR_HEIGHT_DEF

    //Elements
    private var events: List<IEvent>? = null

    //Paints
    private var hourLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)



    init {
        if(attrs != null){
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DayView, 0,0)

            applyAttributes(a)
        }

        hourLinePaint.style = Paint.Style.FILL
        hourLinePaint.color = Color.GRAY
    }

    fun applyAttributes(a: TypedArray){
        try{
            hourHeight = a.getInteger(R.styleable.DayView_hourHeight, HOUR_HEIGHT_DEF)
        }finally {
            a.recycle()
        }
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.d("View", width.toString())

        for(x in 1..24){
            canvas?.drawLine(0f, (x* hourHeight).toFloat() , width.toFloat(), (x* hourHeight).toFloat(), Paint())
        }
    }




    fun setEvents(events:List<IEvent>){
        this.events = events
    }

    fun clearEvents(){
        this.events = null
    }


}