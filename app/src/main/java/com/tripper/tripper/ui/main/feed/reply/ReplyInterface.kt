package com.tripper.tripper.ui.main.feed.reply

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ReplyInterface {
    @GET("/app/feeds/{feedIdx}/comments/{commentIdx}/child-comments")
    fun getReply(
        @Header("x-access-token") jwtToken: String,
        @Path("feedIdx") feedIdx: Int,
        @Path("commentIdx") commentIdx: Int,
        @Query("page") page: Int
    ): Call<ReplyResponse>
}