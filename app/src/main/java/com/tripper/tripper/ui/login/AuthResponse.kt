package com.tripper.tripper.ui.login

import com.google.gson.annotations.SerializedName

data class Auth(val userIdx: Int?,
                val jwt: String?,
                val email: String?,
                val profileImgUrl: String?,
                val kakaoId: String?,
                val ageGroup: String?,
                val gender: String?
                )

data class AuthResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Auth?

    )