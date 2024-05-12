package com.kkyume.android.imagecollecter.model.image

import com.google.gson.annotations.SerializedName

data class ImageRequest(

    @SerializedName("query")
    val query: String = "",

    @SerializedName("sort")
    val sort: String = "",

    @SerializedName("page")
    val page: Int,

    @SerializedName("size")
    val size: Int,
)