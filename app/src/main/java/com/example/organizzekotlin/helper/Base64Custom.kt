package com.example.organizzekotlin.helper

import android.util.Base64

/**
 * Created by jamiltondamasceno
 */
class Base64Custom {
    companion object {

        fun codificarBase64(texto: String): String {
            return Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT)
                .replace("(\\n|\\r)".toRegex(), "")
        }

        fun decodificarBase64(textoCodificado: String?): String {
            return String(Base64.decode(textoCodificado, Base64.DEFAULT))
        }

    }

}