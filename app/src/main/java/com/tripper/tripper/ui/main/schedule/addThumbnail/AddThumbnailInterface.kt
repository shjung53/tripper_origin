package com.tripper.tripper.ui.main.schedule.addThumbnail

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AddThumbnailInterface {
    @Multipart
    @POST("/app/feeds/timage-upload/thumnail")
    fun addThumbnails(
        @Header("x-access-token") xAccessToken: String,
        @Part thumnails: ArrayList<MultipartBody.Part>?
    ): Call<AddThumbnailResponse>
}