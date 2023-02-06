package com.tripper.tripper.ui.splash

interface AutoLoginView {
    fun onAutoLoginSuccess(message: String)
    fun onAutoLoginFailure(code: Int, message: String)
    fun onAutoLoginLoading()
}