package com.tripper.tripper.ui.main.schedule.postFeed
import com.tripper.tripper.ui.main.feed.TravelIdx
import com.google.gson.annotations.SerializedName

data class PostFeedResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: TravelIdx
)
