package com.tripper.tripper.ui.main.feed.reviewInquiry

import com.google.gson.annotations.SerializedName

data class ReviewInquiryResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReviewData
    )

data class ReviewData (
    var travelAreaReviewImage: ArrayList<String>?,
    var travelAreaReviewComment: String?
    )
