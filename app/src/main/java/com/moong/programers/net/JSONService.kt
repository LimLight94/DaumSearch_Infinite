package com.moong.programers.net

import com.moong.programers.data.ItemBean
import com.moong.programers.data.ItemDetailData
import com.moong.programers.data.Res
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JSONService {
    @GET("image")
    fun getList(@Query("sort") type : String , @Query("page") page: Int, @Query("size") size: Int, @Query("query") keyWord: String): Single<ItemBean>
    @GET("products/{id}")
    fun getItemDetail(@Path("id") id: Int) : Single<Res<ItemDetailData>>
}