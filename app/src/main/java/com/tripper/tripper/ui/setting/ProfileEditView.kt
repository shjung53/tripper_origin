package com.tripper.tripper.ui.setting

interface ProfileEditView {
    fun onProfileEditSuccess(message: String)
    fun onProfileEditFailure(code: Int, message: String)
    fun onProfileEditLoading()
}