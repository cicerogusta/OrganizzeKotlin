package com.example.organizzekotlin.util

import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this).replace("R$","R$ ")

}

