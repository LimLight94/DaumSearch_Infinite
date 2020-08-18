package com.moong.programers.net.exception


import com.moong.programers.base.impl.NonActivityInterface
import com.moong.programers.data.Res

class ResultCodeException : Exception, NonActivityInterface {
    var res: Res<*>? = null

    constructor(message: String) : super(message) {}

    constructor(res: Res<*>, message: String) : super(message) {
        this.res = res
    }

    companion object {

        fun create(res: Res<*>?, showPopup: Boolean): ResultCodeException {
            if (res == null) {
                return ResultCodeException("Error on Communicate")
            }

            val rcode = res.errorType
            var message = res.message

            return ResultCodeException(res, "$rcode : $message" )
        }
    }
}
