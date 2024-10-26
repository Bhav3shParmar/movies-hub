package com.app.mvvmdemo.helper

import android.content.Context
import android.widget.Toast

fun Context.myToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}