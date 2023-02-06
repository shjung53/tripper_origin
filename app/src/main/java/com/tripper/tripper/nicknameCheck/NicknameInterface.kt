package com.tripper.tripper.nicknameCheck

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NicknameInterface {
    @GET("/app/users/nickname-check")
    fun nicknameCheck(@Query("nickname") nickname: String): Call<NicknameResponse>
}