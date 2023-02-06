package com.tripper.tripper.ui.login
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var loginView: LoginView

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun login(accessToken: AccessToken) {
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val loginService = retrofit!!.create(LoginInterface::class.java)

        loginView.onLoginLoading()

        loginService.login(accessToken).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, authResponse: Response<AuthResponse>) {
                val response = authResponse.body()!!

                when(response.code){
                    1002 -> loginView.onLoginSuccess(response.result!!)
                    1003 -> loginView.onSignUp(response.message,response.result!!)
                    else -> loginView.onLoginFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                loginView.onLoginFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}