package de.vierheller.todocalendar.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.extensions.getListFromResourceArray
import de.vierheller.todocalendar.model.todo.Priority
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import java.util.*

class TaskActivity : AppCompatActivity() {
    private lateinit var todoViewModel : TodoViewModel

    lateinit var task: MutableLiveData<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(toolbar)

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        parseIntent()

        fab.setOnClickListener { view ->
            todoViewModel.addTodo(task.value!!)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageResArray  = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_images))
        val nameResArray   = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_name))

        settings_list_view.adapter = ListViewAdapter(this, nameResArray, imageResArray, task)
    }



    fun parseIntent(){
        task = MutableLiveData()

        val taskId = intent.getLongExtra(INTENT_ID, -1)
        if(taskId > -1){
            //Getting from DB
            val dbTask = todoViewModel.getTodo(taskId)
            task.setValue(dbTask)
        } else {
            //Initialize with default values
            val newTask = Task(taskName = "", startDate = Calendar.getInstance().timeInMillis, durationMin = 30, priority = Priority.MEDIUM.level)
            task.setValue(newTask)
        }
    }

    companion object {
        val INTENT_ID:String = "id"
    }

}

class ListViewAdapter(val activity: TaskActivity, var names:List<Int>, var icons:List<Int>, var task:LiveData<Task>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(activity)

        var view = convertView

        if(view == null)
            view = inflater.inflate(R.layout.list_item_settings, parent, false)!!

        val tvName  = view.find<TextView>(R.id.settings_name)
        val tvValue = view.find<TextView>(R.id.settings_value)
        val ivImage = view.find<ImageView>(R.id.settings_image)

        tvName.text = activity.getString(getItem(position))

        task.observe(activity, android.arch.lifecycle.Observer { task->
            when(getItem(position)){
                R.string.task_setting_start_time -> {
                    tvValue.text = task?.startDate.toString()
                }

                R.string.task_setting_duration -> {
                    tvValue.text = task?.durationMin.toString()
                }

                R.string.task_setting_buffer -> {
                    tvValue.text = task?.bufferTime.toString()
                }

                R.string.task_setting_priority -> {
                    tvValue.text = task?.priority.toString()
                }
            }
        })

        ivImage.image = activity.getDrawable(icons[position])

        return view
    }

    override fun getItem(position: Int): Int {
        return names[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return names.size
    }
}



