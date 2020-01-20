@file:JvmName("RxExtensions")

package com.moong.programers.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.moong.programers.R
import com.moong.programers.base.impl.AlertInterface
import com.moong.programers.base.impl.NonActivityInterface
import com.moong.programers.base.impl.port.AlertInterfacePort
import com.moong.programers.base.impl.port.AlertInterfacePort.showToast
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

@JvmField
val ignoreError = { t: Throwable ->
    Log.e("ignore", t.message, t)
    if (t !is JsonSyntaxException)
        showToast(AlertInterface.getString(R.string.network_error_msg))
}