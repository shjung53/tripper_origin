package com.tripper.tripper.ui.main.home.feedInquiry
import com.google.gson.annotations.SerializedName

data class FeedInquiryResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<FeedData>
    )

data class FeedData (
    var travelIdx : Int,
    var userIdx : Int,
    var nickName : String,
    var profileImgUrl : String,
    var title : String,
    var introduce : String,
    var myLikeStatus : String,
    var travelHashtag : String?,
    var travelScore : String,
    var thumImgUrl : String,
    var createdAt : String
)
