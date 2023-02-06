package com.tripper.tripper.ui.main.home.feedInquiry

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedInquiryService {

    private lateinit var feedInquiryView: FeedInquiryView

    fun setFeedInquiryView(feedInquiryView: FeedInquiryView){
        this.feedInquiryView = feedInquiryView
    }

    fun getFeedList(jwtToken: String, option: String, page: Int){

        val retrofit = Retrofit.getRetrofit()

        val feedInquiryService = retrofit!!.create(FeedInquiryInterface::class.java)

        feedInquiryView.onFeedInquiryLoading()

        feedInquiryService.getFeedList(jwtToken, option, page).enqueue(object : Callback<FeedInquiryResponse> {
            override fun onResponse(call: Call<FeedInquiryResponse>, feedInquiryResponse: Response<FeedInquiryResponse>) {
                val response = feedInquiryResponse.body()!!

                when(response.code){
                    1032 -> feedInquiryView.onFeedInquirySuccess(response.message,response.result)
                    else -> feedInquiryView.onFeedInquiryFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<FeedInquiryResponse>, t: Throwable) {
                feedInquiryView.onFeedInquiryFailure(400,"네트워크 에러")
            }

        })

    }
}