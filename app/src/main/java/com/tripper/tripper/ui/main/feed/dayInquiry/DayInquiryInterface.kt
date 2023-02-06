package com.tripper.tripper.ui.main.feed.dayInquiry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DayInquiryInterface {
    @GET("/app/feeds/{feedIdx}/search/dayinfo")
    fun getDayData(
        @Header ("x-access-token") jwtToken: String,
        @Path("feedIdx") feedIdx:Int,
        @Query ("day") day: Int
    ): Call<DayInquiryResponse>
}