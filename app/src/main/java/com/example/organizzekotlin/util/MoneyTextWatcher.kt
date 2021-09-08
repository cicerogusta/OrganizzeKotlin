package com.example.organizzekotlin.util

import android.R.attr
import android.widget.EditText

import android.text.Editable

import android.text.TextWatcher
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import android.R.attr.editable
import java.lang.Exception


class MoneyTextWatcher : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText>
    private val locale: Locale

    constructor(editText: EditText?, locale: Locale?) {
        editTextWeakReference = WeakReference<EditText>(editText)
        this.locale = locale ?: Locale.getDefault()
    }

    constructor(editText: EditText?) {
        editTextWeakReference = WeakReference<EditText>(editText)
        locale = Locale.getDefault()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        val editText = editTextWeakReference.get() ?: return

        editText.removeTextChangedListener(this)
        if (s.trim().isNotEmpty()) {
            try {
                val textAsDouble = s.toString().fromCurrency()
                textAsDouble?.let { editText.setText(textAsDouble.toCurrency()) }


            }catch (e: Exception) {

                e.printStackTrace()
            }

        }
        editText.addTextChangedListener(this)


    }
    override fun afterTextChanged(editable: Editable) {

//        val editText = editTextWeakReference.get() ?: return
//        val s = editable.toString()
//        if (s.isEmpty()) return
//        editText.removeTextChangedListener(this)
//        val cleanString = s.replace("[$,.]".toRegex(), "")
//        val parsed = BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
//            .divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
//        val formatted = NumberFormat.getCurrencyInstance().format(parsed)
//        editText.setText(formatted)
//        editText.setSelection(formatted.length)
//        editText.addTextChangedListener(this)

    }

}