package com.moong.programers.net

import com.moong.programers.constants.Constants
import com.moong.programers.constants.Constants.Companion.BASE_URL
import com.moong.programers.data.ItemData
import com.moong.programers.data.ItemDetailData
import com.moong.programers.data.Res
import com.moong.programers.net.compose.ASyncTransformer
import com.moong.programers.net.compose.RWComposer


import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object MainRepository {
    private val interceptor = HttpLoggingInterceptor()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                    .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(Constants.TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                    .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun getService(): JSONService = retrofit.create(JSONService::class.java)

    fun getItemList(skinType: String, page: Int, keyWord: String): Observable<Res<List<ItemData>>> {
        return getService().getList(skinType, page, keyWord)
                .toObservable()
                .compose(RWComposer(true))
                .compose(ASyncTransformer())

    }

    fun getItemList(skinType: String, page: Int): Observable<Res<List<ItemData>>> {
        return getService().getList(skinType, page)
                .toObservable()
                .compose(RWComposer(true))
                .compose(ASyncTransformer())
    }

    fun getItemDetail(id: Int): Observable<Res<ItemDetailData>> {
        return getService().getItemDetail(id)
                .toObservable()
                .compose(ASyncTransformer())
    }
}
