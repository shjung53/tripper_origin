package com.tripper.tripper.ui.setting

import com.google.gson.annotations.SerializedName

data class MembershipWithdrawalResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)