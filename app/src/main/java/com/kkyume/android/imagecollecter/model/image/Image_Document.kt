package com.kkyume.android.imagecollecter.model.image

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class Image_Document(
    @SerializedName("collection")
    val collection: String?,
    @SerializedName("datetime")
    val datetime: Date?,
    @SerializedName("display_sitename")
    val displaySitename: String?,
    @SerializedName("doc_url")
    val docUrl: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,
    @SerializedName("width")
    val width: Int?,





)