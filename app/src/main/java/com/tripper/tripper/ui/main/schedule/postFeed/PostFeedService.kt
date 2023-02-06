package com.tripper.tripper.ui.main.schedule.postFeed

import android.util.Log
import com.tripper.tripper.dataClass.PostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFeedService {

    private lateinit var postFeedView: PostFeedView

    fun setPostFeedView(postFeedView: PostFeedView){
        this.postFeedView = postFeedView
    }

    fun postFeed(jwtToken: String, postData: PostData){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val postFeedService = retrofit!!.create(PostFeedInterface::class.java)

        postFeedView.onPostLoading()

        postFeedService.postFeed(jwtToken,postData).enqueue(object : Callback<PostFeedResponse>{
            override fun onResponse(call: Call<PostFeedResponse>, postFeedResponse: Response<PostFeedResponse>) {
                val response = postFeedResponse.body()!!


                when(response.code){
                    1025 -> postFeedView.onPostSuccess(response.code, response.message, response.result)
                    else -> postFeedView.onPostFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<PostFeedResponse>, t: Throwable) {
                postFeedView.onPostFailure(400,"네트워크 오류가 발생했습니다")
            }
        })
    }









}