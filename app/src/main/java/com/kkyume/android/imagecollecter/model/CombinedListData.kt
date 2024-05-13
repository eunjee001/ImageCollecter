package com.kkyume.android.imagecollecter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Parcelize
data class CombinedListData(
    var thumbnail : String,
    var title : String,
    var category : String,
    var contents : String,
    var dateTime : Date
) : Parcelable
