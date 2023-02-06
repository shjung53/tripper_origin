package com.tripper.tripper.ui.main.feed.dayInquiry

interface DayInquiryView {
    fun onDaySuccess(code: Int, message: String,result: ArrayList<DayData>)
    fun onDayFailure(code: Int, message: String)
    fun onDayLoading()
}