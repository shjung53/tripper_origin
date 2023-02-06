package com.tripper.tripper.ui.main.myInfo.follow


import com.google.gson.annotations.SerializedName

data class FollowResponse(
    @SerializedName ("isSuccess") val isSuccess: Boolean,
    @SerializedName ("code") val code: Int,
    @SerializedName ("message") val message: String

)

