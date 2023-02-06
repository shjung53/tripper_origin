package com.tripper.tripper.ui.main.feed.userProfile

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileService {

    private lateinit var userProfileView: UserProfileView

    fun setUserProfileView(userProfileView: UserProfileView){
        this.userProfileView = userProfileView
    }

    fun userProfile(token: String, userIdx: Int, page: Int){

        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val userProfileService = retrofit!!.create(UserProfileInterface::class.java)

        userProfileView.onUserProfileLoading()

        userProfileService.getUserProfile(token, userIdx, page).enqueue(object : Callback<UserProfileResponse>{
            override fun onResponse(call: Call<UserProfileResponse>, userProfileResponse: Response<UserProfileResponse>) {
                val response = userProfileResponse.body()!!
                when(response.code){
                    1029,3023 -> userProfileView.onUserProfileSuccess(response.code, response.message, response.result)
                    else -> userProfileView.onUserProfileFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                userProfileView.onUserProfileFailure(400, "네트워크 에러")
            }
        })
    }
}