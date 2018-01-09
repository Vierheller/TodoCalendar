package de.vierheller.todocalendar.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.project.Project
import de.vierheller.todocalendar.viewmodel.ProjectsFragmentViewModel
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.withArguments


/**
 * Created by Vierheller on 02.01.2018.
 */
class MyProjectsDialog : DialogFragment(){
    private lateinit var viewModel:ProjectsFragmentViewModel

    lateinit var adapter:SpinnerAdapter

    //Arguments
    private var id:Long = 0
    private lateinit var name:String
    private var parent:Int = -1

    private var listener: ((changed: Boolean, id:Long, name: String, parentPosition: Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProjectsFragmentViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        parseArgs()

        //Inflating custom datepicker view
        val inflater = activity.layoutInflater;
        val view = inflater.inflate(R.layout.dialog_project, null)

        view.find<EditText>(R.id.project_name).setText(name)
        val spinner = view.find<Spinner>(R.id.project_parent_chooser)
        adapter = SpinnerAdapter(activity, android.R.layout.simple_spinner_item)
        spinner.adapter = adapter

        val observer = Observer<List<Project>> {
            adapter.clear()
            adapter.add(activity?.getString(R.string.root_project)?:"Null")
            adapter.addAll(it!!.map { it.name })
            if(parent>-1){
                spinner.setSelection(parent)
            }
        }
        viewModel.getProjects().observe(activity, observer)

        builder.setView(view)
        builder.setTitle(R.string.dialog_fragment_title)
        builder.setPositiveButton(R.string.dialog_accept){ dialogInterface: DialogInterface, i: Int ->
            val newName = view.find<EditText>(R.id.project_name).text.toString()
            val newParentPos = view.find<Spinner>(R.id.project_parent_chooser).selectedItemPosition - 1

            viewModel.getProjects().removeObserver(observer)
            listener?.invoke(false, this.id, newName, newParentPos)
        }
        builder.setNegativeButton(R.string.dialog_cancel){ dialogInterface: DialogInterface, i: Int ->

        }

        return builder.create()
    }

    fun parseArgs(){
        name = arguments.getString(TAG_NAME)
        parent = arguments.getInt(TAG_PARENT)
    }

    fun setListener(listener:(changed: Boolean, id:Long, name: String, parentPosition: Int) -> Unit){
        this.listener = listener
    }

    companion object {
        private val TAG_ID = "ID"
        private val TAG_NAME = "NAME"
        private val TAG_PARENT = "PROJECT"

        fun getInstance(projectId:Long, name:String, parent:Int): MyProjectsDialog {
            return MyProjectsDialog().withArguments(TAG_ID to projectId, TAG_NAME to name, TAG_PARENT to parent)
        }
    }

    class SpinnerAdapter(ctx: Context, res:Int): ArrayAdapter<String>(ctx, res)
}