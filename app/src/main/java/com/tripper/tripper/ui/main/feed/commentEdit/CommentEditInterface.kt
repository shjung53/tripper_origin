package com.tripper.tripper.ui.main.feed.commentEdit


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CommentEditInterface {
    @PATCH("/app/feeds/{feedIdx}/comments/{commentIdx}/change")
    fun commentEdit(
        @Header("x-access-token") jwt: String,
        @Path("feedIdx") feedIdx: Int,
        @Path("commentIdx") commentIdx: Int,
        @Body comment: Comment

    ): Call<CommentEditResponse>
}

data class Comment(
    var comment: String?
)