package com.tripper.tripper.ui.main.home.feedInquiry

import com.tripper.tripper.ui.main.feed.searchComment.CommentInfo

interface FeedInquiryView {
    fun onFeedInquirySuccess(message: String, result: ArrayList<FeedData>)
    fun onFeedInquiryFailure(code: Int, message: String)
    fun onFeedInquiryLoading()
}