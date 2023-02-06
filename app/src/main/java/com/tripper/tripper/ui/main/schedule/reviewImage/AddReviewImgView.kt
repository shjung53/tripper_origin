package com.tripper.tripper.ui.main.schedule.reviewImage

interface AddReviewImgView {
    fun onAddReviewImgSuccess(message: String,result: ArrayList<ReviewImgInfo>)
    fun onAddReviewImgFailure(code: Int, message: String)
    fun onAddReviewImgLoading()
}