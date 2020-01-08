package com.moong.programers.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel
import com.moong.programers.constants.Constants
import com.moong.programers.data.ItemData
import com.moong.programers.net.MainRepository

class MainViewModel
constructor(application: Application) : BaseViewModel(application) {
    val mDataList = ObservableArrayList<ItemData>()
    val mSkinType = ObservableField<String>(Constants.API_SKIN_TYPE_OILY)

    private var currentPage = 1
    private val mMainRepository = MainRepository

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        getItemList()
    }

    private fun getItemList(skinType: String = Constants.API_SKIN_TYPE_OILY, page: Int = 1, keyWord: String = "") {
        if (keyWord.isNotEmpty()) {
            val disposable = mMainRepository.getItemList(skinType, page, keyWord).subscribe({ listBeanRes ->
                listBeanRes.body?.let { mDataList.addAll(it) }
                currentPage=page+1
            }, { t -> t.stackTrace })
            addDisposable(disposable)
        }else{
            val disposable = mMainRepository.getItemList(skinType, page).subscribe({ listBeanRes ->
                listBeanRes.body?.let { mDataList.addAll(it) }
                currentPage=page+1
            }, { t -> t.stackTrace })
            addDisposable(disposable)
        }
    }

    fun loadNextPage(){
        getItemList(mSkinType.get()!!,currentPage)
    }
}
