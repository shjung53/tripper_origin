package com.tripper.tripper.ui.login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginInterface {
    @POST("/app/users/kakao-login")
    fun login(@Body accessToken: AccessToken): Call<AuthResponse>
}