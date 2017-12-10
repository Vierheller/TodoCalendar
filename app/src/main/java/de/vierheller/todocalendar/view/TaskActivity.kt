package de.vierheller.todocalendar.view

import android.app.DatePickerDialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import java.text.DateFormat
import java.text.SimpleDateFormat


class TaskActivity : AppCompatActivity() {
    private lateinit var todoViewModel : TodoViewModel
    private lateinit var listViewAdapter:ListViewAdapter

    lateinit var task: MutableLiveData<Task>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(toolbar)

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        parseIntent()

        fab.setOnClickListener { view ->
            todoViewModel.addTodo(task.value!!)
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageResArray  = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_images))
        val nameResArray   = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_name))

        listViewAdapter = ListViewAdapter(this, nameResArray, imageResArray, task)
        settings_list_view.adapter = listViewAdapter

        settings_list_view.setOnItemClickListener{ adapterView: AdapterView<*>, view: View, pos: Int, id: Long ->
            when(nameResArray[pos]){
                R.string.task_setting_start_time -> {
                    initializeDateTimePicker()
                }

                R.string.task_setting_duration -> {
                }

                R.string.task_setting_buffer -> {
                }

                R.string.task_setting_priority -> {
                }
            }
        }

        activity_task_title.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                task.value!!.taskName = text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        task.observe(this, android.arch.lifecycle.Observer { task ->
            activity_task_title.setText(task!!.taskName)
        })
    }


    fun initializeDateTimePicker(){
        // Initialize
        val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Title example",
                "OK",
                "Cancel"
        )

        // Assign values
        dateTimeDialogFragment.startAtCalendarView()
        dateTimeDialogFragment.set24HoursMode(true)

        // Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()))
        } catch (e: SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException) {
            Log.e(TaskActivity.TAG, e.message)
        }


        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(object : SwitchDateTimeDialogFragment.OnButtonClickListener {
            override fun onPositiveButtonClick(date: Date) {
                task.value!!.startDate = date.time
                listViewAdapter.notifyDataSetChanged()
            }

            override fun onNegativeButtonClick(date: Date) {
                // Date is get on negative button click
            }
        })

        // Show
        dateTimeDialogFragment.show(supportFragmentManager, "dialog_time")
    }



    fun parseIntent(){
        task = MutableLiveData()

        val taskId = intent.getLongExtra(INTENT_ID, -1)
        if(taskId > -1){
            //Getting from DB
            todoViewModel.getTodo(taskId){ dbTask ->
                task.setValue(dbTask)
            }
        } else {
            //Initialize with default values
            val newTask = Task(taskName = "Is this freedom?", startDate = Calendar.getInstance().timeInMillis, durationMin = 30, priority = Priority.MEDIUM.level)
            task.setValue(newTask)
        }
    }

    companion object {
        val TAG : String = TaskActivity::class.java.canonicalName;
        val INTENT_ID:String = "id"

    }

}

class ListViewAdapter(val activity: TaskActivity, var names:List<Int>, var icons:List<Int>, var task:LiveData<Task>): BaseAdapter() {
    val dateFormat:DateFormat = SimpleDateFormat.getDateTimeInstance()

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
                    val date = Date();
                    date.time = task?.startDate!!
                    val dateString = dateFormat.format(date)
                    tvValue.text = dateString
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



