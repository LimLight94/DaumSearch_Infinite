package com.moong.programers.detail

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel
import com.moong.programers.net.MainRepository
import com.moong.programers.net.MainRepository.getItemDetail

class DetailDialogViewModel
constructor(application: Application) : BaseViewModel(application) {
    val mTitle = ObservableField<String>("")
    val mPrice = ObservableField<String>("")
    val mImage = ObservableField<String>("")
    val mDscrption = ObservableField<String>("")


    private val mMainRepository = MainRepository

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getItemDetail(439)
    }

    private fun getItemDetail(id:Int){
        val disposable = mMainRepository.getItemDetail(id).subscribe({ itemBeansRes->
            itemBeansRes.body?.let {
                mTitle.set(it.title)
                mImage.set(it.fullSizeImage)
                mPrice.set(it.price)
                mDscrption.set(it.description)
            }
        },{it.stackTrace })
        addDisposable(disposable)
    }

}
