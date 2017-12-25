package de.vierheller.todocalendar.view.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class TaskViewHolder(
        itemView: View?,
        val title: TextView,
        val date: TextView,
        val time: TextView,
        val viewForeground: View,
        val viewBackground: View) : RecyclerView.ViewHolder(itemView)