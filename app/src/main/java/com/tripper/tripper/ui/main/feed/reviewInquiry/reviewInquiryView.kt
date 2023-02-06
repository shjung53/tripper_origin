package com.tripper.tripper.ui.main.feed.reviewInquiry


interface ReviewInquiryView {
    fun onReviewSuccess(code: Int, message: String,result: ReviewData)
    fun onReviewFailure(code: Int, message: String)
    fun onReviewLoading()
}