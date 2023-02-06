package com.tripper.tripper.ui.main.myInfo.followList

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FollowListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<FollowListResult>
)

@Parcelize
data class FollowListResult(
    var toIdx: Int?,
    var fromIdx: Int?,
    var nickName: String,
    var profileImgUrl: String?,
    var followStatus: String?
):Parcelable
