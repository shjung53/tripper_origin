package com.tripper.tripper.ui.main.feed.searchComment


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentListInterface {
    @GET ("/app/feeds/{feedIdx}/comments-list")
    fun getCommentList(
        @Header("x-access-token") xAccessToken: String,
        @Path("feedIdx") feedIdx: Int,
        @Query("page") page: Int
    ): Call<CommentListResponse>
}