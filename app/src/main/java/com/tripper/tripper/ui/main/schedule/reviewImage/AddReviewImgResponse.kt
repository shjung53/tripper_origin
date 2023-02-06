package com.tripper.tripper.ui.main.schedule.reviewImage
import com.google.gson.annotations.SerializedName

class AddReviewImgResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<ReviewImgInfo>
    )

data class ReviewImgInfo(
    var originalname: String,
    var key: String,
    var location: String,
    var contentType: String
)