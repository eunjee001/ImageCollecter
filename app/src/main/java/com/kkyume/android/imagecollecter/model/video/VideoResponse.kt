package com.kkyume.android.imagecollecter.model.video


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    @SerialName("documents")
    val documents: List<Video_Document>,
    @SerialName("meta")
    val meta: Video_Meta
)