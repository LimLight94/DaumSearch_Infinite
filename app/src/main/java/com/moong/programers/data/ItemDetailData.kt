package com.moong.programers.data

import com.google.gson.annotations.SerializedName

class ItemDetailData {

    @SerializedName("sensitive_score")
    var sensitiveScore: Int = 0
    @SerializedName("dry_score")
    var dryScore: Int = 0
    @SerializedName("oily_score")
    var oilyScore: Int = 0
    @SerializedName("price")
    var price: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("full_size_image")
    var fullSizeImage: String? = null
    @SerializedName("id")
    var id: Int = 0
}
