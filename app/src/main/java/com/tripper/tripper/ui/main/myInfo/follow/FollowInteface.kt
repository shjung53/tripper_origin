package com.tripper.tripper.ui.main.myInfo.follow

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FollowInterface {
    @POST("/app/users/following")
    fun setFollow(
        @Header ("x-access-token") jwt: String,
        @Body toIdx: ToIdx
    ): Call<FollowResponse>
}

data class ToIdx(
    val toIdx: Int?
)
