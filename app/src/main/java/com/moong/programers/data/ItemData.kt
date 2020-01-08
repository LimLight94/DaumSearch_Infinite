package com.moong.programers.data

import com.google.gson.annotations.SerializedName

class ItemData {

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("price")
    var price: String? = null
    @SerializedName("oily_score")
    var oilyScore: Int? = null
    @SerializedName("dry_score")
    var dryScore: Int? = null
    @SerializedName("sensitive_score")
    var sensitiveScore: Int? = null
    @SerializedName("thumbnail_image")
    var thumbnailImage: String? = null
    @SerializedName("title")
    var title: String? = null

}
