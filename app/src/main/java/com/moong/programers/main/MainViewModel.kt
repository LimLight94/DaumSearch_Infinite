package com.moong.programers.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel
import com.moong.programers.constants.Constants
import com.moong.programers.data.ItemData
import com.moong.programers.net.MainRepository
import com.moong.programers.utils.RxUtils.Companion.propertyChanges
import com.moong.programers.utils.ignoreError
import io.reactivex.Observable
import pyxis.uzuki.live.richutilskt.utils.hideKeyboard

class MainViewModel
constructor(application: Application) : BaseViewModel(application) {
    val mDataList = ObservableArrayList<ItemData>()
    val mSkinType = ObservableField(0)
    val mKeyWord = ObservableField<String>("")
    val mIsLoading = ObservableBoolean(false)


    val mTitle = ObservableField<String>("")
    val mPrice = ObservableField<String>("")
    val mImage = ObservableField<String>("")
    val mDscrption = ObservableField<String>("")

    private var currentPage = 1
    private val mMainRepository = MainRepository

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        getItemList()
        checkChange()
    }

    private fun getItemList(page: Int = 1) {
        requireActivity().hideKeyboard()
        mIsLoading.set(true)
        if (mKeyWord.get().toString().isNotEmpty()) {
            val disposable = mMainRepository.getItemList(Constants.API_SKIN_TYPE[mSkinType.get()!!], page, mKeyWord.get().toString())
                    .subscribe({ listBeanRes ->
                        listBeanRes.body?.let { mDataList.addAll(it) }
                        currentPage = page + 1
                        mIsLoading.set(false)
                    }, {
                        ignoreError(it)
                        mIsLoading.set(false)
                    })
            addDisposable(disposable)
        } else {
            val disposable = mMainRepository.getItemList(Constants.API_SKIN_TYPE[mSkinType.get()!!], page).subscribe({ listBeanRes ->
                listBeanRes.body?.let { mDataList.addAll(it) }
                currentPage = page + 1
                mIsLoading.set(false)
            }, {
                ignoreError(it)
                mIsLoading.set(false)
            })
            addDisposable(disposable)
        }
    }

    fun loadNextPage() {
        getItemList(currentPage)
    }

    private fun checkChange() {
        val disposable = Observable.merge(propertyChanges(mSkinType), propertyChanges(mKeyWord)).subscribe {
            mDataList.clear()
            getItemList()
        }
        addDisposable(disposable)
    }

    fun getItemDetail(id: Int, callback: () -> Unit) {
        val disposable = mMainRepository.getItemDetail(id).subscribe({ itemBeansRes ->
            itemBeansRes.body?.let {
                mTitle.set(it.title)
                mImage.set(it.fullSizeImage)
                mPrice.set(it.price)
                mDscrption.set(it.description)
                callback.invoke()
            }
        }, { ignoreError(it) })
        addDisposable(disposable)
    }

}
