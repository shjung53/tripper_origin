package com.tripper.tripper.ui.main.myInfo

import com.google.gson.annotations.SerializedName

data class DeleteFeedResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
