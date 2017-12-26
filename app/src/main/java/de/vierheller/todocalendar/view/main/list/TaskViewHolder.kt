package de.vierheller.todocalendar.view.main.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import de.vierheller.todocalendar.model.todo.Task

class TaskViewHolder(
        itemView: View?,
        val title: TextView,
        val date: TextView,
        val time: TextView,
        val viewForeground: View,
        val viewBackground: View,
        val swipeable:Boolean,
        var task: Task? = null) : RecyclerView.ViewHolder(itemView)