package com.moong.programers.main

import android.app.Application
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.moong.programers.base.BaseViewModel
import com.moong.programers.constants.Constants
import com.moong.programers.data.ItemData
import com.moong.programers.net.MainRepository
import com.moong.programers.utils.RxUtils.Companion.propertyChanges
import com.moong.programers.utils.ShowDialogEvent
import com.moong.programers.utils.ignoreError
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pyxis.uzuki.live.richutilskt.utils.hideKeyboard

class MainViewModel
constructor(application: Application) : BaseViewModel(application) {
    val mDataList = ObservableArrayList<ItemData>()
    val mSkinType = ObservableField(0)
    val mKeyWord = ObservableField<String>("")
    val mIsContent_Loading = ObservableBoolean(false)


    val mTitle = ObservableField<String>("")
    val mPrice = ObservableField<String>("")
    val mImage = ObservableField<String>("")
    val mDscrption = ObservableField<String>("")
    val mIsDetail_Loading = ObservableBoolean(false)

    private var mCurrentPage = 1
    private val mMainRepository = MainRepository

    private lateinit var mDetailDisposable: Disposable

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        getItemList()
        checkChange()
    }

    private fun getItemList(page: Int = 1) {
        requireActivity().hideKeyboard()
        mIsContent_Loading.set(true)
        if (mKeyWord.get().toString().isNotEmpty()) {
            val disposable = mMainRepository.getItemList(Constants.API_SKIN_TYPE[mSkinType.get()!!], page, mKeyWord.get().toString())
                    .subscribe({ listBeanRes ->
                        listBeanRes.body?.let { mDataList.addAll(it) }
                        mCurrentPage = page + 1
                        mIsContent_Loading.set(false)
                    }, {
                        ignoreError(it)
                        mIsContent_Loading.set(false)
                    })
            addDisposable(disposable)
        } else {
            val disposable = mMainRepository.getItemList(Constants.API_SKIN_TYPE[mSkinType.get()!!], page).subscribe({ listBeanRes ->
                listBeanRes.body?.let { mDataList.addAll(it) }
                mCurrentPage = page + 1
                mIsContent_Loading.set(false)
            }, {
                ignoreError(it)
                mIsContent_Loading.set(false)
            })
            addDisposable(disposable)
        }
    }

    fun loadNextPage() {
        getItemList(mCurrentPage)
    }

    private fun checkChange() {
        val disposable = Observable.merge(propertyChanges(mSkinType), propertyChanges(mKeyWord)).subscribe {
            mDataList.clear()
            getItemList()
        }
        addDisposable(disposable)
    }

    fun getItemDetail(id: Int) {
        if (mIsDetail_Loading.get()) {
            mDetailDisposable.dispose()
        }
        mIsDetail_Loading.set(true)
        mDetailDisposable = mMainRepository.getItemDetail(id).subscribe({ itemBeansRes ->
            itemBeansRes.body?.let {
                mTitle.set(it.title)
                mImage.set(it.fullSizeImage)
                mPrice.set(it.price)
                mDscrption.set(it.description)
                mIsDetail_Loading.set(false)
            }
        }, {
            ignoreError(it)
            mIsDetail_Loading.set(false)
        })
        addDisposable(mDetailDisposable)
    }

    fun clickCloseBtn(view : View){
        postEvent(ShowDialogEvent(-1))
    }

}
