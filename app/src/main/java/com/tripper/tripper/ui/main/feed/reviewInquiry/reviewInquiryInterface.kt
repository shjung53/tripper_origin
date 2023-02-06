package com.tripper.tripper.ui.main.feed.reviewInquiry

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewInquiryInterface {
    @GET("/app/feeds/{feedIdx}/search/review")
    fun getReview(
        @Header("x-access-token") jwtToken: String,
        @Path("feedIdx") feedIdx:Int,
        @Query("day") dayIdx: Int,
        @Query("area") area: Int
    ): Call<ReviewInquiryResponse>
}