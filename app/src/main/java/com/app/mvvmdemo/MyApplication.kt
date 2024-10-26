package com.app.mvvmdemo

import android.app.Application
import com.app.mvvmdemo.repositorys.DataRepository
import com.google.android.material.color.DynamicColors

class MyApplication : Application() {

    lateinit var repo: DataRepository
    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)
        repo = DataRepository(this)

    }
}