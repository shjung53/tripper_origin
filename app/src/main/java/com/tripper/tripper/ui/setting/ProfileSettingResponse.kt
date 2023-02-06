package com.tripper.tripper.ui.setting

import com.google.gson.annotations.SerializedName

data class ProfileSettingResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Profile
)

data class Profile(val nickName: String, val profileImgUrl: String)