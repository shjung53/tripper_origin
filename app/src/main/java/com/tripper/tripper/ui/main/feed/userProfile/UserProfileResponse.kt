package com.tripper.tripper.ui.main.feed.userProfile

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: UserProfileResult
)

data class UserProfileResult(
    val otherProfileInfo: OtherProfileInfo,
    val otherProfileFeed: ArrayList<OtherProfileFeed>?
)

data class OtherProfileInfo(
    val profileImgUrl: String,
    val nickName: String,
    val followingCount: Int?,
    val followerCount: Int?,
    val followStatus: String
)

data class OtherProfileFeed(
    val travelIdx: Int,
    val travelTitle: String,
    val travelIntroduce: String,
    val travelHashtag: String?,
    val travelScore: String?,
    var likeStatus: String,
    val thumImgUrl: String?,
    val createdAt: String,
    val userIdx: Int
)
