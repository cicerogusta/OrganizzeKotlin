package com.example.organizzekotlin.util

import android.util.Patterns
import com.example.organizzekotlin.util.Validation.isEmail

object Validation {
    fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

}
