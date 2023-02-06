package com.tripper.tripper.ui.main.feed.userProfile

interface UserProfileView {
    fun onUserProfileSuccess(code: Int, message: String, result: UserProfileResult)
    fun onUserProfileFailure(code: Int, message: String)
    fun onUserProfileLoading()
}