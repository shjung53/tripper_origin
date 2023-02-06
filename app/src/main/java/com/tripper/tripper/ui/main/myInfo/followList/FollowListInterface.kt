package com.tripper.tripper.ui.main.myInfo.followList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowListInterface {
    @GET("/app/users/{userIdx}/follow-list")
    fun getFollowList(
        @Header("x-access-token") jwt: String,
        @Path("userIdx") userIdx: Int,
        @Query("option") option: String
    ): Call<FollowListResponse>
}

