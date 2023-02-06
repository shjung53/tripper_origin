package com.tripper.tripper.ui.main.myInfo.searchMyInfo

interface MyInfoView {
    fun onMyInfoSuccess(message: String, result: UserMyPageResult,code: Int)
    fun onMyInfoFailure(code: Int, message: String)
}