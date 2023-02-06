package com.tripper.tripper.ui.splash

import com.google.gson.annotations.SerializedName

data class AutoLoginResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)