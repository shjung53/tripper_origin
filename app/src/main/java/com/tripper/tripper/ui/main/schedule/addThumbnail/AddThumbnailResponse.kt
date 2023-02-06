package com.tripper.tripper.ui.main.schedule.addThumbnail
import com.google.gson.annotations.SerializedName

data class AddThumbnailResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<ThumbnailInfo>
)

data class ThumbnailInfo(
    var originalname: String,
    var key: String,
    var location: String,
    var contentType: String
)