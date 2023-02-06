package com.tripper.tripper.ui.main.home.feedInquiry

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FeedInquiryInterface {
    @GET("/app/main-page?")
    fun getFeedList(
        @Header("x-access-token") xAccessToken: String,
        @Query("option") option: String,
        @Query("page") page: Int
    ):Call<FeedInquiryResponse>
}