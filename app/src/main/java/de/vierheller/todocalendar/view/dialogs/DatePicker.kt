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
class DatePicker: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        dialog.setPositiveButton(R.string.dialog_accept){ dialog: DialogInterface, id: Int ->

        }

        dialog.setNegativeButton(R.string.dialog_cancel){ dialog: DialogInterface, id: Int ->

        }

        return dialog.create()
    }
}