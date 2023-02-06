package com.tripper.tripper.ui.login
import android.util.Log
import com.tripper.tripper.Retrofit
import com.tripper.tripper.dataClass.SignUpData
import retrofit2.*

class SignUpService {

    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }


    fun signUp(signUpData: SignUpData) {
        val retrofit = Retrofit.getRetrofit()

        val signUpService = retrofit!!.create(SignUpInterface::class.java)

        signUpView.onSignUpLoading()

        signUpService.signUp(signUpData).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, signUpResponse: Response<SignUpResponse>) {
                val response = signUpResponse.body()!!

                when(response.code){
                    1004 -> signUpView.onSignUpSuccess(response.message, response.result!!)
                    else -> signUpView.onSignUpFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다")
            }

        })
    }
}