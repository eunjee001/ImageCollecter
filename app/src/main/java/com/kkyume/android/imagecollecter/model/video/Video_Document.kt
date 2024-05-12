package com.kkyume.android.imagecollecter.model.video


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video_Document(
    @SerialName("author")
    val author: String,
    @SerialName("datetime")
    val datetime: String,
    @SerialName("play_time")
    val playTime: Int,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String
)