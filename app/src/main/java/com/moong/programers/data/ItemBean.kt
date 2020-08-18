package com.moong.programers.data

import com.google.gson.annotations.SerializedName

/**
 * ChallangeProjects
 * Class: ItemBean
 * Created by limmoong on 2020-08-18.
 *
 * Description:
 */
data class ItemBean (
    @SerializedName("documents")
    var documents : List<ItemData>? = null,
    @SerializedName("meta")
    var meta : MetaData? = null
)
