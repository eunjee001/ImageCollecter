package com.kkyume.android.imagecollecter.model.image


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageMeta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)
