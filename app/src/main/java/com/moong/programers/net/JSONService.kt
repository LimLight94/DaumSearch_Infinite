package com.moong.programers.net

import com.moong.programers.data.ItemData
import com.moong.programers.data.ItemDetailData
import com.moong.programers.data.Res
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JSONService {
    @GET("products")
    fun getList(@Query("skin_type") skinType : String , @Query("page") page: Int, @Query("search") keyWord: String): Single<Res<List<ItemData>>>
    @GET("products")
    fun getList(@Query("skin_type") skinType : String , @Query("page") page: Int): Single<Res<List<ItemData>>>
    @GET("products/{id}")
    fun getItemDetail(@Path("id") id: Int) : Single<Res<ItemDetailData>>
}