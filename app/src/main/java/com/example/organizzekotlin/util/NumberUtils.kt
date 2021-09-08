package com.example.organizzekotlin.util

import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this).replace("R$","R$ ")

}

fun String.fromCurrency(): Double? {
    return try {
        this.toPlainString().parsePlainToDouble()
    } catch (e: Exception) {
        null
    }
}

fun String.toPlainString(): String {
    return this.replace("[.,R$\\s]".toRegex(), "")
}

fun String.parsePlainToDouble(): Double {
    val value = this.toDouble()
    return value.div(100)
}






