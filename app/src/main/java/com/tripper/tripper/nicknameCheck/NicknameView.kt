package com.tripper.tripper.nicknameCheck

interface NicknameView {

    fun onNicknameSuccess(message: String)
    fun onNicknameFailure(code: Int, message: String)
    fun onNicknameLoading()
}