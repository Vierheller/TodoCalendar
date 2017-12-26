package de.vierheller.todocalendar.view.main.list.extra

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.graphics.drawable.Drawable
import de.vierheller.todocalendar.R


/**
 * Created by Vierheller on 18.12.2017.
 */
class SimpleDividerItemDecoration: RecyclerView.ItemDecoration {
    private var mDivider: Drawable

    constructor(context:Context) {
        mDivider = context.resources.getDrawable(R.drawable.line_divider, context.theme)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount-1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}