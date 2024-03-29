package com.tripper.tripper.nicknameCheck

import com.google.gson.annotations.SerializedName

data class NicknameResponse (

    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)