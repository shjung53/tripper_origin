package com.tripper.tripper.ui.main.myInfo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangeStatusService {

    private lateinit var changeStatusView: ChangeStatusView

    fun setChangeStatusView(changeStatusView: ChangeStatusView){
        this.changeStatusView = changeStatusView
    }

    fun changeStatus(token: String,feedIdx: Int){

        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val changeStatusService = retrofit!!.create(ChangeStatusInterface::class.java)

        changeStatusView.onChangeStatusLoading()

        changeStatusService.changeStatus(token, feedIdx).enqueue(object : Callback<ChangeStatusResponse> {
            override fun onResponse(call: Call<ChangeStatusResponse>, changeStatusResponse: Response<ChangeStatusResponse>) {
                val response = changeStatusResponse.body()!!
                when(response.code) {
                    1023, 1024 -> changeStatusView.onChangeStatusSuccess(response.message)
                    else -> changeStatusView.onChangeStatusFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ChangeStatusResponse>, t: Throwable) {
                changeStatusView.onChangeStatusFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}