package com.example.organizzekotlin.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

/**
 * Created by jamiltondamasceno
 */
object DateCustom {
    @SuppressLint("SimpleDateFormat")
    fun dataAtual(): String {
        val data = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(data)
    }

    fun mesAnoDataEscolhida(data: String): String {
        val retornoData = data.split("/").toTypedArray()
        val dia = retornoData[0] //dia 23
        val mes = retornoData[1] //mes 01
        val ano = retornoData[2] //ano 2018
        return mes + ano
    }
}