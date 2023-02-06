package com.tripper.tripper.ui.main.myInfo.searchMyInfo

import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @SerializedName ("isSuccess") var isSuccess : Boolean,
    @SerializedName ("code") var code : Int,
    @SerializedName ("message") var message: String,
    @SerializedName ("result") var result: UserMyPageResult
)

data class UserMyPageResult(
    var userMyPageInfo:UserMyPageInfo,
    var userMyPageFeedByOption: ArrayList<UserMyPageFeedByOption>?

)

data class UserMyPageInfo(
    var profileImgUrl: String,
    var nickName: String,
    var followingCount: Int,
    var followerCount: Int
)

data class UserMyPageFeedByOption(
    var travelIdx: Int,
    var userIdx: Int,
    var travelTitle: String,
    var travelIntroduce: String,
    var travelStatus: String?,
    var travelHashtag: String?,
    var travelScore: String?,
    var travelThumnail: String?,
    var travelCreatedAt: String,
    var travelLikeStatus: String?
)

