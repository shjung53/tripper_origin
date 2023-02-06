package com.tripper.tripper.ui.main.schedule.reviewImage

interface DeleteReviewImgView {
    fun onDeleteReviewImgSuccess(message: String)
    fun onDeleteReviewImgFailure(code: Int, message: String)
    fun onDeleteReviewImgLoading()
}