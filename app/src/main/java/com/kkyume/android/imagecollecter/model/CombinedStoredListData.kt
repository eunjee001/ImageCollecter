package com.kkyume.android.imagecollecter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

data class CombinedStoredListData(
    val thumbnailUrl: String?,
    val title: String?,
    val category: String?,
    val contents: String?,
    val date: Date?,
    val type: ItemType?,
    val id: String?,
    val isSelect : Boolean = true
)
