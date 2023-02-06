package com.tripper.tripper.ui.main.myInfo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteFeedService {

    private lateinit var deleteFeedView: DeleteFeedView

    fun setDeleteFeedView(deleteFeedView: DeleteFeedView){
        this.deleteFeedView = deleteFeedView
    }

    fun deleteFeed(token: String, feedIdx: Int){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val deleteFeedService = retrofit!!.create(DeleteFeedInterface::class.java)
        deleteFeedView.onDeleteFeedLoading()

        deleteFeedService.deleteFeed(token, feedIdx).enqueue(object : Callback<DeleteFeedResponse> {
            override fun onResponse(call: Call<DeleteFeedResponse>, deleteFeedResponse: Response<DeleteFeedResponse>) {
                val response = deleteFeedResponse.body()!!
                when(response.code){
                    1022 -> deleteFeedView.onDeleteFeedSuccess(response.message)
                    else -> deleteFeedView.onDeleteFeedFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<DeleteFeedResponse>, t: Throwable) {
                deleteFeedView.onDeleteFeedFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}