package com.kkyume.android.imagecollecter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

data class CombinedListData(
    val thumbnailUrl: String?,
    val title: String?,
    val category: String?,
    val contents: String?,
    val date: Date?,
    val type: ItemType?, // 이미지 또는 비디오를 나타내는 타입
    val id: String?, // 고유 식별자
)

enum class ItemType {
    IMAGE, VIDEO
}