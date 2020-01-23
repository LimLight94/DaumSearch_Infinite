package com.moong.programers

import android.app.Application
import com.moong.programers.base.ActivityReference

class MainApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        ActivityReference.initialize(this)
    }
}