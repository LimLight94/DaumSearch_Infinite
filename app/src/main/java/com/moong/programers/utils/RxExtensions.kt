@file:JvmName("RxExtensions")

package com.moong.programers.utils

import android.util.Log
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * ChallangeProject
 * Class: RxExtensions
 * Created by appg on 2020-01-13.
 *
 * Description:
 */
@CheckReturnValue
fun <T> Observable<T>.subscribe(callback: (T?, Throwable?) -> Unit): Disposable {
    return this.subscribe({
        callback.invoke(it, null)
    }, {
        callback.invoke(null, it)
    })
}

@JvmField
val ignoreError = Consumer<Throwable> { t -> Log.e("error", t.message, t) }