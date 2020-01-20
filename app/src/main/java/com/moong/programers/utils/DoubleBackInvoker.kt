package com.moong.programers.utils

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.moong.programers.base.impl.AlertInterface.getString
import com.moong.programers.base.impl.NonActivityInterface
import pyxis.uzuki.live.richutilskt.utils.reboot

/**
 * ChallangeProject
 * Class: DoubleBackInvoker
 * Created by appg on 2020-01-20.
 *
 * Description:
 */
object DoubleBackInvoker : NonActivityInterface {
    private var doubleBackToExitPressedOnce = false

    /**
     * to use this class, ensure using [ActivityReference] feature
     *
     * to use autoFinish, add <activity android:name=".utils.DoubleBackInvoker$FinishActivity"/>
     * into AndroidManifest.xml
     *
     * @param resId resource id of msg to notify user such as 'Press once again to end the app.'
     * @param time optional, if user invoke this method twice within time which provided,
     * return as true. default is 2000
     * @param autoFinish optional, if return is 'true', finish all stacks. default is true
     * @return true if is ok to close
     */
    @JvmStatic
    @JvmOverloads
    fun execute(@StringRes resId: Int, time: Long = 2000, autoFinish: Boolean = true): Boolean {
        return execute(getString(resId), time, autoFinish)
    }

    /**
     * to use this class, ensure using [ActivityReference] feature
     *
     * to use autoFinish, add <activity android:name=".utils.DoubleBackInvoker$FinishActivity"/>
     * into AndroidManifest.xml
     *
     * @param msg msg to notify user such as 'Press once again to end the app.'
     * @param time optional, if user invoke this method twice within time which provided,
     * return as true. default is 2000
     * @param autoFinish optional, if return is 'true', finish all stacks. default is true
     * @return true if is ok to close
     */
    @JvmStatic
    @JvmOverloads
    fun execute(msg: String, time: Long = 2000, autoFinish: Boolean = true): Boolean {
        val activity = requireActivity()

        if (doubleBackToExitPressedOnce) {
            if (autoFinish) {
                activity.reboot(Intent(activity, FinishActivity::class.java))
            }
            return true
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, time)
        return false
    }

    class FinishActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            finishAffinity()
        }
    }
}