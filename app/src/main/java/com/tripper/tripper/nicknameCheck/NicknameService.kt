package com.tripper.tripper.nicknameCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameService {
    private lateinit var nicknameView: NicknameView

    fun setNicknameView(nicknameView: NicknameView){
        this.nicknameView = nicknameView
    }

    fun nicknameCheck (nickName: String){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val nickNameService = retrofit!!.create(NicknameInterface::class.java)

        nicknameView.onNicknameLoading()

        nickNameService.nicknameCheck(nickName).enqueue(object : Callback<NicknameResponse>{
            override fun onResponse(call: Call<NicknameResponse>, nickNameResponse: Response<NicknameResponse>) {
                val response = nickNameResponse.body()!!
                when(response.code){
                    1005 -> nicknameView.onNicknameSuccess(response.message)
                    else -> nicknameView.onNicknameFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                nicknameView.onNicknameFailure(400, "네트워크 오류가 발생했습니다")
            }
        })


    }
}