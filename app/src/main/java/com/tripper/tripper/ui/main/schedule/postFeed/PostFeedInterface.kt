package com.tripper.tripper.ui.main.schedule.postFeed

import com.tripper.tripper.dataClass.PostData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PostFeedInterface {
    @POST("/app/feeds")
    fun postFeed(
        @Header("x-access-token") jwtToken: String,
        @Body postData: PostData
    ) : Call<PostFeedResponse>
}