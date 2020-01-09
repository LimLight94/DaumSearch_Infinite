package com.moong.programers.detail

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel

class DetailDialogViewModel
constructor(application: Application) : BaseViewModel(application) {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

}
