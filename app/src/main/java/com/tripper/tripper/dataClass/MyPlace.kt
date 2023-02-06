package com.tripper.tripper.dataClass
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPlace(
    val placeType: Int = 0 ,
    var name: String = "",
    var address: String = ""
):Parcelable
