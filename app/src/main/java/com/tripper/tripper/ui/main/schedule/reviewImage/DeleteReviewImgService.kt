package com.tripper.tripper.ui.main.schedule.reviewImage

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteReviewImgService {

    private  lateinit var deleteReviewImgView: DeleteReviewImgView

    fun setDeleteReviewImgView(deleteReviewImgView: DeleteReviewImgView){
        this.deleteReviewImgView = deleteReviewImgView
    }



    fun deleteImg(xAccessToken: String, imgkey: String, dirname: String): Int{
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val deleteReviewImgService = retrofit!!.create(DeleteReviewImgInterface::class.java)

        deleteReviewImgView.onDeleteReviewImgLoading()

        deleteReviewImgService.deleteImg(xAccessToken, imgkey, dirname).enqueue(object : Callback<DeleteReviewImgResponse>{
            override fun onResponse(call: Call<DeleteReviewImgResponse>, deleteReviewImgResponse: Response<DeleteReviewImgResponse>) {
                val response = deleteReviewImgResponse.body()!!

                when(response.code){
                    1017 -> deleteReviewImgView.onDeleteReviewImgSuccess(response.message)
                    else -> deleteReviewImgView.onDeleteReviewImgFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<DeleteReviewImgResponse>, t: Throwable) {
                deleteReviewImgView.onDeleteReviewImgFailure(400,"네트워크 오류가 발생했습니다")
            }
        })


        return 1
    }




}