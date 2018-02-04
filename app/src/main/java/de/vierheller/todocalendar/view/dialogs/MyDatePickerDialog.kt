package de.vierheller.todocalendar.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import de.vierheller.todocalendar.R

/**
 * Created by Vierheller on 06.12.2017.
 */
class MyDatePickerDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        //Inflating custom datepicker view
        val inflater = activity!!.layoutInflater;
        val view = inflater.inflate(R.layout.dialog_datepicker, null)

        //Adding view to dialog
        builder.setView(view)

        builder.setPositiveButton(R.string.dialog_accept){ dialog: DialogInterface, id: Int ->

        }

        builder.setNegativeButton(R.string.dialog_cancel){ dialog: DialogInterface, id: Int ->

        }

        return builder.create()
    }
}