package de.vierheller.todocalendar.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.NumberPicker
import de.vierheller.todocalendar.R
import org.jetbrains.anko.find
import org.jetbrains.anko.layoutInflater

/**
 * Created by Vierheller on 11.12.2017.
 */
class MyNumberPickerDialog() : DialogFragment() {
    var numberPicker: NumberPicker? = null
    var listener: ((new: Int) -> Unit)? = null
    var defaultValue:Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = context.layoutInflater.inflate(R.layout.dialog_numberpicker, null)
        numberPicker = view.find<NumberPicker>(R.id.dialog_number_picker)
        numberPicker.minValue = 0
        numberPicker.maxValue = 100

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setPositiveButton(R.string.positive_button_dialog){ dialogInterface: DialogInterface, i: Int ->
            val newNumber = numberPicker!!.value
            listener?.invoke(newNumber)
        }
        builder.setNegativeButton(R.string.negative_button_dialog){ dialogInterface: DialogInterface, i: Int ->
            //Nothing to do
        }

        return builder.create()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.find<NumberPicker>(R.id.dialog_number_picker)!!.value = defaultValue
    }

    fun setValue(value:Int){
        if(numberPicker != null)
            numberPicker!!.value = value
        else
            defaultValue = value
    }

    fun setOnValueChangedListener(new_listener: (new:Int) -> Unit){
        this.listener = new_listener
    }
}