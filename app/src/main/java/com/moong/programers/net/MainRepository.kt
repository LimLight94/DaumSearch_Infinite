package com.moong.programers.net

import com.moong.programers.constants.Constants.Companion.BASE_URL
import com.moong.programers.data.ItemData
import com.moong.programers.data.Res


import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object MainRepository {
    private val interceptor = HttpLoggingInterceptor()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun getService(): JSONService = retrofit.create(JSONService::class.java)

    fun getItemList(skinType: String, page: Int, keyWord: String): Observable<Res<List<ItemData>>> {
        return getService().getList(skinType, page, keyWord)
                .toObservable()
                .compose(ASyncTransformer())
    }
    fun getItemList(skinType: String, page: Int): Observable<Res<List<ItemData>>> {
        return getService().getList(skinType, page)
                .toObservable()
                .compose(ASyncTransformer())
    }
}