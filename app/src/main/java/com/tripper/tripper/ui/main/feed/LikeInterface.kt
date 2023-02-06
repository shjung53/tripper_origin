package com.tripper.tripper.ui.main.feed

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LikeInterface {
    @POST("/app/feeds/like")
    fun like(
        @Header("x-access-token") xAccessToken: String,
        @Body travelIdx: TravelIdx
    ): Call<LikeResponse>
}

data class TravelIdx(
    val travelIdx: Int?
)