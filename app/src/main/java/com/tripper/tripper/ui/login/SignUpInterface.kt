package com.tripper.tripper.ui.login
import com.tripper.tripper.dataClass.SignUpData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpInterface {
    @POST("/app/users/sign-up")
    fun signUp(@Body signUpData: SignUpData) : Call<SignUpResponse>

}