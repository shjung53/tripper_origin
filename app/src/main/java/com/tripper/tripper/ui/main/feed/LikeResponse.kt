package com.tripper.tripper.ui.main.feed

import com.google.gson.annotations.SerializedName

data class LikeResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)