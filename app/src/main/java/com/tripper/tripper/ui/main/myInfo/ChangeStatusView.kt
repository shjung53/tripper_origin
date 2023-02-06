package com.tripper.tripper.ui.main.myInfo

interface ChangeStatusView {
    fun onChangeStatusSuccess(message: String)
    fun onChangeStatusFailure(code: Int, message: String)
    fun onChangeStatusLoading()
}