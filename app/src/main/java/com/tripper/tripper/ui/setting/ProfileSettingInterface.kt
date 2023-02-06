package com.tripper.tripper.ui.setting

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileSettingInterface {
    @GET("/app/users/profile-setting")
    fun profileSetting(@Header("x-access-token") token: String): Call<ProfileSettingResponse>
}