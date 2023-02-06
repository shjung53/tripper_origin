package com.tripper.tripper.ui.main.feed.deleteComment

interface DeleteCommentView {
    fun onDeleteCommentSuccess(message: String)
    fun onDeleteCommentFailure(code: Int, message: String)
    fun onDeleteCommentLoading()
}