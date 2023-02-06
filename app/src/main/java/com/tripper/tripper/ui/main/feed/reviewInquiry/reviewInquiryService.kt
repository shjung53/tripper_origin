package com.tripper.tripper.ui.main.feed.reviewInquiry

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewInquiryService {
    private lateinit  var reviewInquiryView: ReviewInquiryView

    fun setReviewInquiryView(reviewInquiryView: ReviewInquiryView){
        this.reviewInquiryView = reviewInquiryView
    }

    fun getReview(token: String, feedIdx: Int, dayIdx: Int, area: Int){
        val retrofit = Retrofit.getRetrofit()

        val reviewInquiryService = retrofit!!.create(ReviewInquiryInterface::class.java)

        reviewInquiryView.onReviewLoading()

        reviewInquiryService.getReview(token,feedIdx, dayIdx, area).enqueue(object :
            Callback<ReviewInquiryResponse>{
            override fun onResponse(call: Call<ReviewInquiryResponse>, reviewInquiryResponse: Response<ReviewInquiryResponse>) {
               val response = reviewInquiryResponse.body()
                when(response!!.code){
                    1034 -> reviewInquiryView.onReviewSuccess(response.code, response.message, response.result)
                    else -> reviewInquiryView.onReviewFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ReviewInquiryResponse>, t: Throwable) {
                reviewInquiryView.onReviewFailure(400,"네트워크 에러")
            }

        })

    }
}