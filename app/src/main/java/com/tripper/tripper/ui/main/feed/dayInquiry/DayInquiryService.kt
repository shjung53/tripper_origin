package com.tripper.tripper.ui.main.feed.dayInquiry

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayInquiryService {
    private lateinit var dayInquiryView: DayInquiryView

    fun setDayInquiryView(dayInquiryView: DayInquiryView){
        this.dayInquiryView = dayInquiryView
    }

    fun getDayInfo(token: String, feedIdx: Int, day: Int){
        val retrofit = Retrofit.getRetrofit()

        val dayInquiryService = retrofit!!.create(DayInquiryInterface::class.java)

        dayInquiryView.onDayLoading()

        dayInquiryService.getDayData(token, feedIdx, day).enqueue(object :
            Callback<DayInquiryResponse> {
            override fun onResponse(call: Call<DayInquiryResponse>, dayInquiryResponse: Response<DayInquiryResponse>) {
                val response = dayInquiryResponse.body()
                when (response!!.code) {
                    1033 -> dayInquiryView.onDaySuccess(response.code, response.message, response.result)
                    else -> dayInquiryView.onDayFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<DayInquiryResponse>, t: Throwable) {
                dayInquiryView.onDayFailure(400,"네트워크 에러")
            }

        })
    }

}