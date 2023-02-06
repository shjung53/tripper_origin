package com.tripper.tripper.ui.setting

import com.google.gson.annotations.SerializedName

data class ProfileEditResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
