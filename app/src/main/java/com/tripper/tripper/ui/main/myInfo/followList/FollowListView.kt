package com.tripper.tripper.ui.main.myInfo.followList

interface FollowListView {

    fun onFollowListSuccess(message:String, result: ArrayList<FollowListResult>,code: Int)
    fun onFollowListFailure(code: Int, message: String)
    fun onFollowListLoading()
}