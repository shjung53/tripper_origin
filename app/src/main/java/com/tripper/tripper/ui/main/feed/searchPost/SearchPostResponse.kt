package com.tripper.tripper.ui.main.feed.searchPost

import com.google.gson.annotations.SerializedName

data class SearchFeedResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SearchPostResult
)

data class SearchPostResult(
    val travelThumnails: ArrayList<String?>,
    val travelInfo: TravelInfo,
    val travelDayIdx: ArrayList<Int>

)

data class TravelInfo(
    val travelIdx: Int,
    val userIdx: Int,
    val profileImgUrl: String,
    val nickName: String,
    val title: String,
    val introduce: String,
    val travelHashtag: String?,
    val travelScore: String,
    val userScoreCount: Int,
    val totalCommentCount: Int,
    val totalLikeCount:Int,
    val myLikeStatus: String?,
    val myScore: String?,
    val createdAt: String,
    var startDate : String,
    var endDate : String
)
