package com.kkyume.android.imagecollecter.model.video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    @SerializedName("documents")
    val documents: List<Video_Document>,
    @SerializedName("meta")
    val meta: Video_Meta
)