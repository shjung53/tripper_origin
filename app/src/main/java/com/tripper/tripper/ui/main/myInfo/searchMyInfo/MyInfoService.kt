package com.tripper.tripper.ui.main.myInfo.searchMyInfo

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoService {

    private lateinit var myInfoView: MyInfoView
    fun setMyInfoView(myInfoView: MyInfoView){
        this.myInfoView = myInfoView
    }

    fun myPageInfo(jwt: String, search: String, page: Int){

        val retrofit = Retrofit.getRetrofit()

        val myInfoService = retrofit!!.create(MyInfoInterface::class.java)

        myInfoService.getMyInfo(jwt, search, page).enqueue(object: Callback<MyInfoResponse>{
            override fun onResponse(
                call: Call<MyInfoResponse>,
                myInfoResponse: Response<MyInfoResponse>
            ) {
                val response = myInfoResponse.body()!!

                when(response.code){
                    1028,3022 -> myInfoView.onMyInfoSuccess(response.message, response.result,response.code)
                    else ->myInfoView.onMyInfoFailure(response.code, response.message)
                }

            }

            override fun onFailure(call: Call<MyInfoResponse>, t: Throwable) {
                myInfoView.onMyInfoFailure(400, "네트워크 에러")
            }
        })

    }

}