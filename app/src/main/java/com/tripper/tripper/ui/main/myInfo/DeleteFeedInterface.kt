package com.tripper.tripper.ui.main.myInfo

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface DeleteFeedInterface {
    @PATCH("/app/feeds/{feedIdx}/deletion")
    fun deleteFeed(
        @Header("x-access-token") xAccessToken: String,
        @Path("feedIdx") feedIdx: Int
    ): Call<DeleteFeedResponse>
}