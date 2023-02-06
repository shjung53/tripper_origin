package com.tripper.tripper.ui.main.feed.deleteComment

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface DeleteCommentInterface {
    @PATCH("/app/feeds/{feedIdx}/comments/{commentIdx}/deletion")
    fun deleteComment(
        @Header("x-access-token") xAccessToken: String,
        @Path("feedIdx") feedIdx: Int,
        @Path("commentIdx") commentIdx: Int
    ): Call<DeleteCommentResponse>
}