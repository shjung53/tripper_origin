package com.tripper.tripper.ui.splash

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutoLoginService {

    private lateinit var autoLoginView: AutoLoginView

    fun setAutoLoginView(autoLoginView: AutoLoginView){
        this.autoLoginView = autoLoginView
    }

    fun autoLogin(xAccessToken: String) {
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val autoLoginService = retrofit!!.create(AutoLoginInterface::class.java)

        autoLoginView.onAutoLoginLoading()

        autoLoginService.autoLogin(xAccessToken).enqueue(object : Callback<AutoLoginResponse> {
            override fun onResponse(call: Call<AutoLoginResponse>, autoLoginResponse: Response<AutoLoginResponse>) {
                val response = autoLoginResponse.body()!!
                when(response.code) {
                    1006 -> autoLoginView.onAutoLoginSuccess(response.message)
                    else -> autoLoginView.onAutoLoginFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<AutoLoginResponse>, t: Throwable) {
                autoLoginView.onAutoLoginFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}