package com.moong.programers.detail

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel
import pyxis.uzuki.live.richutilskt.utils.runDelayed

class DetailViewModel
constructor(application: Application) : BaseViewModel(application) {
    val str = ObservableField<String>("시작 텍스트")

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        runDelayed(5000) { str.set("텍스트변화")}
    }
}
