package com.tripper.tripper.ui.setting

interface ProfileSettingView {
    fun onProfileSettingSuccess(message: String, profile: Profile)
    fun onProfileSettingFailure(code: Int, message: String)
    fun onProfileSettingLoading()
}