package com.tripper.tripper.ui.setting

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MembershipWithdrawalService {

    private lateinit var membershipWithdrawalView: MembershipWithdrawalView

    fun setMembershipWithdrawalView(membershipWithdrawalView: MembershipWithdrawalView){
        this.membershipWithdrawalView = membershipWithdrawalView
    }


    fun membershipWithdrawal(jwtToken: String, accessToken: String){
        val retrofit = Retrofit.getRetrofit()

        val membershipWithdrawalService = retrofit!!.create(MembershipWithdrawalInterface::class.java)
        membershipWithdrawalView.onMembershipWithdrawalLoading()

        membershipWithdrawalService.membershipWithdrawal(jwtToken, accessToken).enqueue(object :
            Callback<MembershipWithdrawalResponse> {
            override fun onResponse(call: Call<MembershipWithdrawalResponse>, membershipWithdrawalResponse: Response<MembershipWithdrawalResponse>) {

                val response = membershipWithdrawalResponse.body()!!
                when(response.code){
                    1036 -> membershipWithdrawalView.onMembershipWithdrawalSuccess(response.message)
                    else -> membershipWithdrawalView.onMembershipWithdrawalFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<MembershipWithdrawalResponse>, t: Throwable) {
                membershipWithdrawalView.onMembershipWithdrawalFailure(400, "네트워크 오류가 발생했습니다")
            }

        })
    }
}