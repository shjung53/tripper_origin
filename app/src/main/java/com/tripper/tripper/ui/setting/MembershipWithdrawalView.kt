package com.tripper.tripper.ui.setting

interface MembershipWithdrawalView {
    fun onMembershipWithdrawalSuccess(message: String)
    fun onMembershipWithdrawalFailure(code: Int, message: String)
    fun onMembershipWithdrawalLoading()
}