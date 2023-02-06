package com.tripper.tripper.dataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatesData(
    val date: String
): Parcelable