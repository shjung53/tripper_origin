package com.tripper.tripper.ui.main.feed

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ScoreInterface {
    @POST("/app/feeds/score")
    fun score(
        @Header("x-access-token") xAccessToken: String,
        @Body scoreData: ScoreData
    ): Call<ScoreResponse>
}

data class ScoreData(
    val travelIdx: Int,
    val score: Int
)