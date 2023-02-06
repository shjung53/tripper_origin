package com.tripper.tripper.ui.main.schedule.reviewImage

import com.google.gson.annotations.SerializedName

class DeleteReviewImgResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)