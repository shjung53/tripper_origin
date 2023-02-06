package com.tripper.tripper.ui.setting

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MembershipWithdrawalInterface {
    @PATCH("/app/users/withdraw")
    fun membershipWithdrawal(
        @Header("x-access-token") jwtToken: String,
        @Header("accessToken") accessToken: String
    ) : Call<MembershipWithdrawalResponse>
}