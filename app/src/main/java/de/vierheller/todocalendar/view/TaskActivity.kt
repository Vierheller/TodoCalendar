package de.vierheller.todocalendar.view

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.extensions.getListFromResourceArray
import de.vierheller.todocalendar.model.todo.Priority
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.view.dialogs.MyNoteWriterDialog
import de.vierheller.todocalendar.view.dialogs.MyNumberPickerDialog
import de.vierheller.todocalendar.viewmodel.TaskActivityViewModel
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.content_task.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TaskActivity : AppCompatActivity() {
    private lateinit var listViewAdapter:ListViewAdapter
    private lateinit var viewModel : TaskActivityViewModel
    private lateinit var observer: Observer<Task>


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(TaskActivityViewModel::class.java)
        viewModel.init(intent)

        fab.setOnClickListener { view ->
            viewModel.save()
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageResArray  = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_images))
        val nameResArray   = getListFromResourceArray(resources.obtainTypedArray(R.array.task_settings_name))

        listViewAdapter = ListViewAdapter(this, viewModel, nameResArray, imageResArray)
        settings_list_view.adapter = listViewAdapter

        settings_list_view.setOnItemClickListener{ adapterView: AdapterView<*>, view: View, pos: Int, id: Long ->
            when(nameResArray[pos]){
                R.string.task_setting_start_time -> {
                    initializeDateTimePicker()
                }

                R.string.task_setting_duration -> {
                    val numberDialogDuration = MyNumberPickerDialog.newInstance(R.string.task_setting_duration,
                            0,
                            30,
                            viewModel.getTask().value?.durationMin!!)
                    numberDialogDuration.setOnValueChangedListener { new ->
                        viewModel.setDuration(new)
                    }
                    numberDialogDuration.show(supportFragmentManager, "DurationDialog")
                }

                R.string.task_setting_buffer -> {
                    val numberDialogBuffer = MyNumberPickerDialog.newInstance(R.string.task_setting_buffer,
                            0,
                            100,
                            viewModel.getTask().value?.bufferTime!!)
                    numberDialogBuffer.setOnValueChangedListener { new ->
                        viewModel.setBuffer(new)
                    }
                    numberDialogBuffer.show(supportFragmentManager, "BufferDialog")
                }

                R.string.task_setting_priority -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.task_setting_priority)
                    builder.setItems(
                            Priority.asStringArray(this),
                            {dialogInterface, i ->
                                viewModel.setPriorityFromIndex(i)
                            }
                        )
                    val dialog = builder.create()
                    dialog.show()
                }

                R.string.task_setting_note ->{
                    val noteDialog = MyNoteWriterDialog.newInstance(R.string.task_setting_note,
                            viewModel.getTask().value?.note!!)
                    noteDialog.setListener { new ->
                        viewModel.setNote(new)
                    }
                    noteDialog.show(supportFragmentManager, "NoteDialog")
                }
            }
        }

        activity_task_title.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                viewModel.setTaskName(text.toString())
                Log.d(TAG, "Text changed to ${text}")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        //Add this observer once to initialize the textview with tasks name
        //Remove it afterwards since the value will always be up to date
        this.observer = android.arch.lifecycle.Observer<Task> { task ->
            activity_task_title.setText(task!!.taskName)
            viewModel.getTask().removeObserver(this.observer)
        }
        viewModel.getTask().observe(this,observer)

        //Once the value was changed, the ListView has to be updated.
        //Otherwise changes would not be displayed
        viewModel.getTask().observe(this, android.arch.lifecycle.Observer { task ->
            listViewAdapter.notifyDataSetChanged()
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
                viewModel.setStartDate(date)
            }

            override fun onNegativeButtonClick(date: Date) {
                // Date is get on negative button click
            }
        })

        // Show
        dateTimeDialogFragment.show(supportFragmentManager, "dialog_time")
    }

    companion object {
        val TAG : String = TaskActivity::class.java.canonicalName
        val INTENT_ID:String = "id"

    }

}

class ListViewAdapter(val activity: TaskActivity, val viewModel: TaskActivityViewModel, var names:List<Int>, var icons:List<Int>): BaseAdapter() {
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

        viewModel.getTask().observe(activity, android.arch.lifecycle.Observer { task->
            Log.d("observing", task?.taskName?:"Is null")
            when(getItem(position)){
                R.string.task_setting_start_time -> {
                    val date = Date()
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
                    val prioString: String
                    val stringRes = Priority.fromLevel(task?.priority!!)?.stringRes?:0
                    if(stringRes!=0){
                        prioString = activity.getString(stringRes)
                    }else{
                        prioString = "Undefinded"
                    }
                    tvValue.text = prioString
                }
                R.string.task_setting_note->{
                    val note = task?.note
                    if(!note.isNullOrEmpty()){
                        tvValue.text = "\"${task?.note}\""
                        tvValue.maxLines = 1
                        tvValue.ellipsize = TextUtils.TruncateAt.END
                    }else{
                        tvValue.text = activity.getString(R.string.task_setting_note_placeholder)
                    }
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



