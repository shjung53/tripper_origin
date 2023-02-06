package com.tripper.tripper.ui.setting

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.kakao.sdk.auth.TokenManagerProvider
import com.tripper.tripper.databinding.ActivityAccountBinding
import com.tripper.tripper.ui.login.LoginActivity
import com.kakao.sdk.user.UserApiClient
import com.tripper.tripper.*
import gun0912.tedimagepicker.util.ToastUtil.context

class AccountActivity: AppCompatActivity(), MembershipWithdrawalView {
    private lateinit var binding: ActivityAccountBinding

    private lateinit var membershipWithdrawalService: MembershipWithdrawalService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        membershipWithdrawalService = MembershipWithdrawalService()
        membershipWithdrawalService.setMembershipWithdrawalView(this)

        val layoutInflater = LayoutInflater.from(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_account, null)

        val alertDialog = AlertDialog.Builder(this, R.style.NewDialog)
            .setView(dialogView)
            .create()

        val dialogDeleteBtn = dialogView.findViewById<CardView>(R.id.dialog_delete_account_delete_cv)
        val dialogCancelBtn = dialogView.findViewById<CardView>(R.id.dialog_delete_account_cancel_cv)

        dialogCancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        val token = getJwt(this)

        dialogDeleteBtn.setOnClickListener {
//            if(token != ""){
//                UserApiClient.instance.unlink { error ->
//                    if (error != null) {
//                        Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this, LoginActivity::class.java)
//                        startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
//                        finish()
//                    } }
//                }else{
//                Toast.makeText(this,"로그인 후 이용가능한 서비스 입니다",Toast.LENGTH_SHORT).show()
//            }
            val accessToken = TokenManagerProvider.instance.manager.getToken()!!.accessToken
            membershipWithdrawalService.membershipWithdrawal(token, accessToken)
        }

        binding.accountDeleteAccountCl.setOnClickListener {
            alertDialog.show()
        }

        binding.accountBackIv.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMembershipWithdrawalSuccess(message: String) {
        LoadingDialog.loadingOff()
        val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf.edit()
        editor.remove("jwt")
        editor.remove("auth")
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
    }

    override fun onMembershipWithdrawalFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMembershipWithdrawalLoading() {
        LoadingDialog.loadingOn(this)
    }
}