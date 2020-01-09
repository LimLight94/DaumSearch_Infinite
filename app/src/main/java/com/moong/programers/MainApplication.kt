package com.moong.programers

import android.app.Application
import com.moong.programers.base.ActivityReference

/**
 * ChallangeProject
 * Class: MainApplication
 * Created by appg on 2020-01-09.
 *
 * Description:
 */
class MainApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        ActivityReference.initialize(this)
    }
}