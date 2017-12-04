package de.vierheller.todocalendar.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.vierheller.todocalendar.R
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

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
        val namesArray = resources.getStringArray(R.array.task_settings_name);
        val imageArray = resources.obtainTypedArray(R.array.task_settings_images);
        val intArray = MutableList<Int>(imageArray.length()) { i ->
            imageArray.getResourceId(i, -1)
        }
        imageArray.recycle()
        settingslistview.adapter = ListViewAdapter(this, namesArray, intArray)
    }



}

class ListViewAdapter(val context: Context, var names:Array<String>, var icons:MutableList<Int>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = LayoutInflater.from(context);

        var view = convertView

        if(view == null)
            view = inflator.inflate(R.layout.list_item_settings, parent, false);

        val tvName = view!!.find<TextView>(R.id.settings_name);
        val ivImage = view!!.find<ImageView>(R.id.settings_image)
        tvName.text = getItem(position).toString()
        Log.d("images", icons.toString())
        ivImage.image = context.getDrawable(icons[position])
        return view;
    }

    override fun getItem(position: Int): Any {
        return names[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return names.size;
    }
}



