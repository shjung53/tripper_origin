package com.tripper.tripper.ui.main.schedule.reviewImage

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Query

interface DeleteReviewImgInterface {
    @DELETE("/app/feeds/timage-delete")
    fun deleteImg(
        @Header("x-access-token") jwtToken: String,
        @Header("imgkey") imgkey: String,
        @Query("dirname") dirname: String
    ) : Call<DeleteReviewImgResponse>

}