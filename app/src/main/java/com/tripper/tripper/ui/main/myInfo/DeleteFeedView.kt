package com.tripper.tripper.ui.main.myInfo

interface DeleteFeedView {
    fun onDeleteFeedSuccess(message: String)
    fun onDeleteFeedFailure(code: Int, message: String)
    fun onDeleteFeedLoading()
}