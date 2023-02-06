package com.tripper.tripper.ui.login

interface SignUpView {

    fun onSignUpSuccess(message: String, auths: Auths)
    fun onSignUpFailure(code: Int, message: String)
    fun onSignUpLoading()
}