package com.tripper.tripper.ui.main.schedule.reviewImage

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewImgService {

    private lateinit var addReviewImgView: AddReviewImgView

    fun setAddReviewImgView(addReviewImgView: AddReviewImgView) {
        this.addReviewImgView = addReviewImgView
    }


    fun addReviewImg(xAccessToken: String, reviewImgs: ArrayList<MultipartBody.Part>?) {
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val addReviewImgService = retrofit!!.create(AddReviewImgInterface::class.java)

        addReviewImgView.onAddReviewImgLoading()

        addReviewImgService.addReviewImgs(xAccessToken, reviewImgs).enqueue(object : Callback<AddReviewImgResponse> {
                override fun onResponse(call: Call<AddReviewImgResponse>, addReviewResponse: Response<AddReviewImgResponse>) {
                    val response = addReviewResponse.body()!!
                    when(response.code){
                        1016 -> addReviewImgView.onAddReviewImgSuccess(response.message, response.result)
                        else -> addReviewImgView.onAddReviewImgFailure(response.code, response.message)
                    }
                }

                override fun onFailure(call: Call<AddReviewImgResponse>, t: Throwable) {
                    addReviewImgView.onAddReviewImgFailure(400,"네트워크 오류가 발생했습니다")
                }


            })
    }
}