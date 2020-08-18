package com.moong.programers.constants


interface Constants {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/v2/search/"
        const val TIME_OUT = 10000
        val API_TYPE = arrayOf("accuracy","recency")
    }
}
