package com.moong.programers.net

import com.moong.programers.constants.Constants
import com.moong.programers.constants.Constants.Companion.BASE_URL
import com.moong.programers.data.ItemBean
import com.moong.programers.data.ItemData
import com.moong.programers.data.ItemDetailData
import com.moong.programers.data.Res
import com.moong.programers.net.compose.ASyncTransformer
import com.moong.programers.net.compose.RWComposer


import io.reactivex.Observable
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object MainRepository {
    private val interceptor = HttpLoggingInterceptor()

    private val header = Interceptor { chain ->
           val newRequest = chain.request().newBuilder().addHeader("Authorization", "KakaoAK 1620c531520002ce7d22db089084b6fa").build()
        chain.proceed(newRequest)
    }


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(header)
                .readTimeout(Constants.TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .build()
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getService(): JSONService = retrofit.create(JSONService::class.java)

    fun getItemList(
        type: String,
        page: Int,
        keyWord: String = "kakao"
    ): Observable<ItemBean> {
        return getService().getList(type, page, 20, keyWord)
            .toObservable()
            .compose(ASyncTransformer())

    }

    fun getItemDetail(id: Int): Observable<Res<ItemDetailData>> {
        return getService().getItemDetail(id)
            .toObservable()
            .compose(ASyncTransformer())
    }
}
