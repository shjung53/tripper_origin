package com.tripper.tripper.ui.login

interface LoginView {

    fun onLoginSuccess(auth: Auth)
    fun onLoginFailure(code: Int, message: String)
    fun onSignUp(message: String, auth: Auth)
    fun onLoginLoading()
}