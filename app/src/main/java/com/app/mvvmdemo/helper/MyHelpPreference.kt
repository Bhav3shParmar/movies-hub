package com.app.mvvmdemo.helper

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object MyHelpPreference {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("adultPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    var isAdult: Boolean
        get() = sharedPreferences.getBoolean("isAdult", true)
        set(value) = editor.putBoolean("isAdult", value).apply()
}