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

            val rcode = res.statusCode
            var message = "오류 발생 "
            when (rcode) {
                400 -> message += ": 클라이언트 요청오류"
                404 -> message += ": 조회한 데이터 없음"
                500 -> message += ": 서버 오류"
            }

            return ResultCodeException(res, message)
        }
    }
}
