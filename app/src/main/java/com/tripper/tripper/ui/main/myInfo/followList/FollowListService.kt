package com.tripper.tripper.ui.main.myInfo.followList

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowListService {

    private lateinit var followListView: FollowListView

    fun setFollowListView(followListView: FollowListView){
        this.followListView = followListView
    }

    fun followList(jwt: String, userIdx: Int, option: String ){

        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val followListService = retrofit!!.create(FollowListInterface::class.java)

        followListView.onFollowListLoading()

        followListService.getFollowList(jwt,userIdx,option).enqueue(object : Callback<FollowListResponse>{
            override fun onResponse(
                call: Call<FollowListResponse>,
                followResponse: Response<FollowListResponse>
            ) {
                val response = followResponse.body()!!

                when(response.code){
                    1009 -> followListView.onFollowListSuccess(response.message, response.result, response.code)
                    1010 -> followListView.onFollowListSuccess(response.message, response.result, response.code)
                    else -> followListView.onFollowListFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<FollowListResponse>, t: Throwable) {
                t.printStackTrace()
                followListView.onFollowListFailure(400, "네트워크 에러")

            }

        })
    }
}