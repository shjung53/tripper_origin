package com.tripper.tripper.ui.main.feed

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeService {

    private lateinit var likeView: LikeView

    fun setLikeView(likeView: LikeView){
        this.likeView = likeView
    }

    fun like(token: String,travelIdx: TravelIdx) {
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val likeService = retrofit!!.create(LikeInterface::class.java)

        likeService.like(token,travelIdx).enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, likeResponse: Response<LikeResponse>) {
                val response = likeResponse.body()!!
                when(response.code) {
                    1018,1019 -> likeView.onLikeSuccess(response.code, response.message)
                    else -> likeView.onLikeFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                likeView.onLikeFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}