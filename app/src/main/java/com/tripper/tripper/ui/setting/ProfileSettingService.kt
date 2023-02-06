package com.tripper.tripper.ui.setting

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileSettingService {

    private lateinit var profileSettingView: ProfileSettingView

    fun setProfileSettingView(profileSettingView: ProfileSettingView){
        this.profileSettingView = profileSettingView
    }

    fun profileSetting(token: String) {
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        profileSettingView.onProfileSettingLoading()

        val profileSettingService = retrofit!!.create(ProfileSettingInterface::class.java)
        profileSettingService.profileSetting(token).enqueue(object : Callback<ProfileSettingResponse> {
            override fun onResponse(call: Call<ProfileSettingResponse>, profileSettingResponse: Response<ProfileSettingResponse>) {
                val response = profileSettingResponse.body()!!
                when(response.code) {
                    1012 -> profileSettingView.onProfileSettingSuccess(response.message, response.result)
                    else -> profileSettingView.onProfileSettingFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ProfileSettingResponse>, t: Throwable) {
                t.printStackTrace()
                profileSettingView.onProfileSettingFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}