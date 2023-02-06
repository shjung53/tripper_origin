package com.tripper.tripper.ui.main.schedule.reviewImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AddReviewImgInterface {
    @Multipart
    @POST("/app/feeds/timage-upload/travel")
    fun addReviewImgs(
        @Header("x-access-token") xAccessToken: String,
        @Part travels: ArrayList<MultipartBody.Part>?
    ): Call<AddReviewImgResponse>

}