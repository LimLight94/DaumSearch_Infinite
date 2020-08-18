package com.moong.programers.data

import com.google.gson.annotations.SerializedName

data class ItemData(

    @SerializedName("collection")
    var collection: String? = null,
    @SerializedName("datetime")
    var datetime: String? = null,
    @SerializedName("display_sitename")
    var displaySitename: String? = null,
    @SerializedName("doc_url")
    var docUrl: String? = null,
    @SerializedName("height")
    var height: Long? = null,
    @SerializedName("image_url")
    var imageUrl: String? = null,
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String? = null,
    @SerializedName("width")
    var width: Long? = null

)
