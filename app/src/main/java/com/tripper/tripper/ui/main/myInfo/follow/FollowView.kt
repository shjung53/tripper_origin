package com.tripper.tripper.ui.main.myInfo.follow

interface FollowView {
    fun onFollowSuccess(message:String)
    fun onFollowFailure(code: Int, message: String)
    fun onFollowLoading()
}