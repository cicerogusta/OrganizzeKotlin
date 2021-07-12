package com.example.organizzekotlin.util

import android.text.TextUtils
import android.util.Patterns

fun String.isEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()