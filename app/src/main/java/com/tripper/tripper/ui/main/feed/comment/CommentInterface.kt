package com.tripper.tripper.ui.main.feed.comment

import com.tripper.tripper.dataClass.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CommentInterface {
    @POST("/app/feeds/comments")
    fun postComment(
        @Header("x-access-token") jwtToken: String,
        @Body commentData: CommentData
    ): Call<CommentResponse>
}

data class CommentData (
    var travelIdx : Int,
    var comment : String,
    var isParent : Int?
    )


