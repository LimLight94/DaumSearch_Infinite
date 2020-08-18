package com.moong.programers.data

import com.google.gson.annotations.SerializedName

data class MetaData (
    @SerializedName("is_end")
    var isEnd: Boolean,
    @SerializedName("pageable_count")
    var pageableCount: Long,
    @SerializedName("total_count")
    var totalCount: Long
)
