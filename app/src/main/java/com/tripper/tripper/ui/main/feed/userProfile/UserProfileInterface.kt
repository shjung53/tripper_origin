package com.tripper.tripper.ui.main.feed.userProfile

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserProfileInterface {
    @GET("/app/users/{userIdx}/profile")
    fun getUserProfile(
        @Header("x-access-token") xAccessToken: String,
        @Path("userIdx") userIdx: Int,
        @Query("page") page: Int
    ): Call<UserProfileResponse>
}