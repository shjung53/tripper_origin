package com.tripper.tripper.ui.main.feed.comment

import com.tripper.tripper.ui.main.feed.TravelIdx

interface CommentView {
    fun onCommentSuccess(code: Int, message: String, result: CommentIdx)
    fun onCommentFailure(code: Int, message: String)
    fun onCommentLoading()
}