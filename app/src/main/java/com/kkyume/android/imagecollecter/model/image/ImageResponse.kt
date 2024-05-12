package com.kkyume.android.imagecollecter.model.image


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerialName("documents")
    val documents: List<Image_Document>,
    @SerialName("meta")
    val meta: ImageMeta
)