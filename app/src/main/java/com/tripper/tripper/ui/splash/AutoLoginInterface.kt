package com.tripper.tripper.ui.splash

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AutoLoginInterface {
    @GET("/app/users/auto-login")
    fun autoLogin(@Header("x-access-token") xAccessToken: String): Call<AutoLoginResponse>
}