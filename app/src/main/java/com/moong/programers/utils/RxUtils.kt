package com.moong.programers.utils

import android.util.Log
import androidx.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.functions.Consumer

/**
 * ChallangeProject
 * Class: RxUtils
 * Created by appg on 2020-01-13.
 *
 * Description:
 */
class RxUtils<T>(private val observableString: ObservableField<T>) : Observable<T>() {

    override fun subscribeActual(observer: Observer<in T>) {
        val listener = Listener(observableString, observer)
        observer.onSubscribe(listener)
        observableString.addOnPropertyChangedCallback(listener.onPropertyChangedCallback)
    }

    private inner class Listener internal constructor(
            private val observableData: ObservableField<T>,
            observer: Observer<in T>?
    ) : MainThreadDisposable() {
        internal val onPropertyChangedCallback: androidx.databinding.Observable.OnPropertyChangedCallback

        init {
            this.onPropertyChangedCallback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(observable: androidx.databinding.Observable?, propertyId: Int) {
                    if (!isDisposed) {
                        observableData.get()?.let { observer?.onNext(it) }
                    }
                }
            }
        }

        override fun onDispose() {
            observableString.removeOnPropertyChangedCallback(onPropertyChangedCallback)
        }
    }

    companion object {
        @JvmStatic
        fun <T> propertyChanges(target: ObservableField<T>) = RxUtils(target)
    }
}