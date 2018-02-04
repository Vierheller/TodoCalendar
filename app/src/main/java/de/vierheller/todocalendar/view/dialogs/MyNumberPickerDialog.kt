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
class MyNumberPickerDialog : DialogFragment() {
    lateinit var numberPicker: NumberPicker
    var listener: ((new: Int) -> Unit)? = null

    var titleResource:Int = -1
    var defaultValue:Int = -1
    var minValue:Int = -1
    var maxValue:Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parseArguments()

        val view = context!!.layoutInflater.inflate(R.layout.dialog_numberpicker, null)
        numberPicker = view.find<NumberPicker>(R.id.dialog_number_picker)
        numberPicker.minValue = minValue
        numberPicker.maxValue = maxValue
        numberPicker.value = defaultValue

        val builder = AlertDialog.Builder(context)
        builder.setTitle(titleResource)
        builder.setView(view)
        builder.setPositiveButton(R.string.dialog_accept){ dialogInterface: DialogInterface, i: Int ->
            val newNumber = numberPicker.value
            listener?.invoke(newNumber)
        }
        builder.setNegativeButton(R.string.dialog_cancel){ dialogInterface: DialogInterface, i: Int ->

        }

        return builder.create()
    }

    fun parseArguments(){
        val args = arguments!!
        titleResource = args.getInt(ARG_TITLE)
        minValue = args.getInt(ARG_MIN_VALUE)
        maxValue = args.getInt(ARG_MAX_VALUE)
        defaultValue = args.getInt(ARG_DEFAULT_VALUE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.find<NumberPicker>(R.id.dialog_number_picker).value = defaultValue
    }

    fun setValue(value:Int){
        numberPicker.value = value
    }

    fun setOnValueChangedListener(new_listener: (new:Int) -> Unit){
        this.listener = new_listener
    }

    companion object {
        val ARG_TITLE = "TITLE";
        val ARG_MIN_VALUE = "MIN_VALUE"
        val ARG_MAX_VALUE = "MAX_VALUE"
        val ARG_DEFAULT_VALUE = "DEFAULT_VALUE"

        fun newInstance(title:Int, minValue:Int, maxValue:Int, defaultValue:Int): MyNumberPickerDialog {
            if(defaultValue < minValue ||  defaultValue > maxValue)
                throw IllegalArgumentException()

            val dialog = MyNumberPickerDialog();
            val args = Bundle();

            args.putInt(ARG_TITLE, title)
            args.putInt(ARG_MIN_VALUE, minValue)
            args.putInt(ARG_MAX_VALUE, maxValue)
            args.putInt(ARG_DEFAULT_VALUE, defaultValue)

            dialog.arguments = args
            return dialog
        }
    }
}