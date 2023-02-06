package com.tripper.tripper.ui.login

import com.google.gson.annotations.SerializedName


data class SignUpResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Auths?
        )

data class Auths(
    val token: Token,
    val userInfo: ArrayList<UserInfo>
)

data class Token (
    val userIdx: Int?,
    val jwt: String?
)

data class UserInfo(
    val userIdx: Int?,
    val email: String?,
    val nickName: String?,
    val profileImgUrl: String?,
    val kakaoId: String?,
    val ageGroup: String?,
    val gender: String?
)