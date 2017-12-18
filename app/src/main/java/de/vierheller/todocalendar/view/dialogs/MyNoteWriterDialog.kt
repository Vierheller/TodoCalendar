package de.vierheller.todocalendar.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import de.vierheller.todocalendar.R
import org.jetbrains.anko.find

/**
 * Created by Vierheller on 18.12.2017.
 */
class MyNoteWriterDialog: DialogFragment(){
    private  var title:Int = -1
    private lateinit var text:String

    private lateinit var edit_note:EditText

    private var listener : ((new: String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parseArguments()
        val builder = AlertDialog.Builder(activity)

        //Inflating custom datepicker view
        val inflater = activity.layoutInflater;
        val view = inflater.inflate(R.layout.dialog_note, null)

        //Adding default values
        this.edit_note = view.find<EditText>(R.id.dialog_note_text)
        this.edit_note.setText(text)

        //Adding view to dialog
        builder.setView(view)

        builder.setTitle(title)
        builder.setPositiveButton(R.string.dialog_accept){ dialog: DialogInterface, id: Int ->
            listener?.invoke(edit_note.text.toString())
        }

        builder.setNegativeButton(R.string.dialog_cancel){ dialog: DialogInterface, id: Int ->

        }

        return builder.create()
    }

    fun setListener(nListener:((new: String) -> Unit)){
        this.listener = nListener
    }


    fun parseArguments(){
        val args = arguments

        title = args.getInt(ARG_TITLE)
        text = args.getString(ARG_TEXT)
    }

    companion object {
        val ARG_TITLE:String = "TITLE"
        val ARG_TEXT:String = "TEXT"

        fun newInstance(title:Int, text:String):MyNoteWriterDialog{
            val dialog = MyNoteWriterDialog();
            val args = Bundle();

            args.putInt(MyNoteWriterDialog.ARG_TITLE, title)
            args.putString(MyNoteWriterDialog.ARG_TEXT, text)

            dialog.arguments = args
            return dialog
        }
    }
}