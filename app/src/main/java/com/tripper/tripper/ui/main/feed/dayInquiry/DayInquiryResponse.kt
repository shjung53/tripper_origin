package com.tripper.tripper.ui.main.feed.dayInquiry

import com.google.gson.annotations.SerializedName

data class DayInquiryResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<DayData>
)

data class DayData(
    val areaIdx: Int?,
    val dayIdx: Int,
    val areaCategory: String?,
    val areaName: String?,
    val areaLatitude: Double?,
    val areaLongitude: Double?
)
