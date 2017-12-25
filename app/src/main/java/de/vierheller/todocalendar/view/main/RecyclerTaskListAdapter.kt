package de.vierheller.todocalendar.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Task
import org.jetbrains.anko.find
import java.util.*

class RecyclerTaskListAdapter (var items:List<Task>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: ((View) -> Unit)? = null

    fun setOnClickListener(listener:(View)->Unit){
        this.listener = listener
    }

    fun removeOnClicklistener(){
        this.listener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val masterView: View

        masterView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_item_task, parent, false)



        val title   = masterView.find<TextView>(R.id.item_list_task_title)
        val date    = masterView.find<TextView>(R.id.item_list_task_time_start)
        val time    = masterView.find<TextView>(R.id.item_list_task_time_hour)
        val viewForeground = masterView.find<View>(R.id.list_item_foreground)
        val viewBackground = masterView.find<View>(R.id.list_item_background)
        val viewForegroundHolder = masterView.find<View>(R.id.list_item_foreground_holder)
        viewForegroundHolder.setOnClickListener{
            listener?.invoke(masterView)
        }

        return TaskViewHolder(masterView, title, date, time, viewForeground, viewBackground)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val task: Task = items!![position]

        val curHolder = holder as TaskViewHolder
        curHolder.title.text = task.taskName
        curHolder.date.text = ListViewFragment.dateFormat.format(Date(task.startDate))
        curHolder.time.text = ListViewFragment.timeFormat.format(Date(task.startDate))
    }

    override fun getItemId(position: Int): Long {
        return items?.get(position)?.uid!!
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }


}