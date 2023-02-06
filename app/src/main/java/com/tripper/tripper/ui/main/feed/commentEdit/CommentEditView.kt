package com.tripper.tripper.ui.main.feed.commentEdit

interface CommentEditView {
    fun onCommentEditSuccess(message: String)
    fun onCommentEditFailure(code: Int, message: String)
    fun onCommentEditLoading()
}