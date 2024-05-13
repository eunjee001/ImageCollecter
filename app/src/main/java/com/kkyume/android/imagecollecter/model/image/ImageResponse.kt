package com.kkyume.android.imagecollecter.model.image


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerializedName("meta")
    val meta: ImageMeta,
    @SerializedName("documents")
    val documents: MutableList<Image_Document>,

)