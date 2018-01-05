package de.vierheller.todocalendar.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import android.widget.Spinner
import de.vierheller.todocalendar.R
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.withArguments


/**
 * Created by Vierheller on 02.01.2018.
 */
class MyProjectsDialog : DialogFragment() {
    private var listener: ((changed: Boolean, name: String, parent: String) -> Unit)? = null

    private lateinit var name:String
    private lateinit var parent:String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        parseArgs()

        //Inflating custom datepicker view
        val inflater = activity.layoutInflater;
        val view = inflater.inflate(R.layout.dialog_project, null)

        builder.setView(view)
        builder.setTitle(R.string.dialog_fragment_title)
        builder.setPositiveButton(R.string.dialog_accept){ dialogInterface: DialogInterface, i: Int ->
            val newName = view.find<EditText>(R.id.project_name).text.toString()
            val newParent = view.find<Spinner>(R.id.project_parent_chooser)
            listener?.invoke(false, newName, parent)
        }

        builder.setNegativeButton(R.string.dialog_cancel){ dialogInterface: DialogInterface, i: Int ->

        }

        return builder.create()
    }

    fun parseArgs(){
        name = arguments.getString(TAG_NAME)
        parent = arguments.getString(TAG_PARENT)
    }

    fun setListener(listener:(changed: Boolean, name: String, parent: String) -> Unit){
        this.listener = listener
    }

    companion object {
        private val TAG_NAME = "NAME"
        private val TAG_PARENT = "PROJECT"

        fun getInstance(name:String, parent:Int): MyProjectsDialog {
            return MyProjectsDialog().withArguments(TAG_NAME to name, TAG_PARENT to parent)
        }
    }
}