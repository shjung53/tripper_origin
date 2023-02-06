package com.tripper.tripper.ui.main.feed

interface LikeView {
    fun onLikeSuccess(code: Int, message: String)
    fun onLikeFailure(code: Int, message: String)
    fun onLikeLoading()
}