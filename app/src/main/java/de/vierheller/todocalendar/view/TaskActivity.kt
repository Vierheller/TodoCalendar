package de.vierheller.todocalendar.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.vierheller.todocalendar.R
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.jetbrains.anko.find

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingslistview.adapter = ListViewAdapter(this, resources.getStringArray(R.array.task_settings).toList())
    }



}

class ListViewAdapter(val context: Context, var objects:List<String>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = LayoutInflater.from(context);

        var view = convertView

        if(view == null)
            view = inflator.inflate(R.layout.list_item_settings, parent, false);

        view!!.find<TextView>(R.id.name).text = getItem(position).toString()

        return view;
    }

    override fun getItem(position: Int): Any {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return objects.size;
    }
}



