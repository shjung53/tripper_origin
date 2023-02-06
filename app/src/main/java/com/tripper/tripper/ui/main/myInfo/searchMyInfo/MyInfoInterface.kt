package com.tripper.tripper.ui.main.myInfo.searchMyInfo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MyInfoInterface {
    @GET("/app/users/profile")
    fun getMyInfo(
        @Header ("x-access-token") jwt: String,
        @Query ("search") search: String,
        @Query ("page") page: Int
    ): Call<MyInfoResponse>
}


