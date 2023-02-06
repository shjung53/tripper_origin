package com.tripper.tripper.ui.main.myInfo.follow

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowService {
    private lateinit var followView: FollowView

    fun setFollowView(followView: FollowView){
        this.followView = followView
    }


    fun follow(jwt: String,toIdx: ToIdx){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()


        val followService = retrofit!!.create(FollowInterface::class.java)

        followView.onFollowLoading()

        followService.setFollow(jwt, toIdx).enqueue(object : Callback<FollowResponse>{
            override fun onResponse(
                call: Call<FollowResponse>,
                followResponse: Response<FollowResponse>
            ) {
                val response = followResponse.body()!!

                when(response.code){
                    1007,1008 -> followView.onFollowSuccess(response.message)
                    else -> followView.onFollowFailure(response.code, response.message)

                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                followView.onFollowFailure(400,"네트워크 에러")
            }
        })
    }
}