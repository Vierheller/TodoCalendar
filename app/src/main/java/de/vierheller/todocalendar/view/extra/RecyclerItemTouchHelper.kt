package de.vierheller.todocalendar.view.extra

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import de.vierheller.todocalendar.view.main.list.TaskViewHolder

/**
 * Created by Vierheller on 18.12.2017.
 */
class RecyclerItemTouchHelper(dragDirs: Int,
                              swipeDirs: Int,
                              val listener: (RecyclerView.ViewHolder, Int, Int) -> Unit) :  ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {


    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        val holder = (viewHolder as TaskViewHolder)
        if (!holder.swipeable)
            listener.invoke(holder, direction, holder.getAdapterPosition());
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as TaskViewHolder).viewForeground

            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val holder = (viewHolder as TaskViewHolder)
        val foregroundView = holder.viewForeground
        if (!holder.swipeable)
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        val foregroundView = (viewHolder as TaskViewHolder).viewForeground
        getDefaultUIUtil().clearView(foregroundView);
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val holder = (viewHolder as TaskViewHolder)
        val foregroundView = holder.viewForeground
        if (!holder.swipeable)
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }


}