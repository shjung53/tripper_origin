package com.tripper.tripper.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tripper.tripper.*
import com.tripper.tripper.dataClass.SignUpData
import com.tripper.tripper.databinding.ActivityProfileSettingBinding
import com.tripper.tripper.nicknameCheck.NicknameService
import com.tripper.tripper.nicknameCheck.NicknameView
import gun0912.tedimagepicker.util.ToastUtil.context

class ProfileSettingActivity : AppCompatActivity(), NicknameView, SignUpView {

    lateinit var binding: ActivityProfileSettingBinding
    private lateinit var nicknameService: NicknameService
    private lateinit var signUpService: SignUpService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nicknameService = NicknameService()
        signUpService = SignUpService()
        nicknameService.setNicknameView(this)
        signUpService.setSignUpView(this)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)


//      닉네임 중복 확인 버튼
        binding.profileSettingDuplicationCheckOffTv.setOnClickListener {
            val tryNickname = binding.profileSettingNicknameEt.text.toString()
            nicknameService.nicknameCheck(tryNickname)
        }
//      텍스트 변환시 버튼 변화
        textChangeListener()

//      회원가입 api 연결
        binding.profileSettingCompleteCv.setOnClickListener {
            val nickName = binding.profileSettingNicknameEt.text.toString()
            val email = getEmail(context)
            val profileImgUrl = getProfileImgUrl(context)
            val kakaoId = getKakaoId(context)
            val ageGroup = getAgeGroup(context)
            val gender = getGender(context)
            val signUpData = SignUpData(email, profileImgUrl, kakaoId, ageGroup, gender, nickName)
            signUpService.signUp(signUpData)
        }

        setContentView(binding.root)
    }

    private fun textChangeListener() {
        binding.profileSettingNicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.profileSettingDuplicationCheckOffTv.visibility = View.VISIBLE
                binding.profileSettingDuplicationCheckOnTv.visibility = View.GONE
                binding.profileSettingPossibleTv.visibility = View.GONE
                binding.profileSettingNicknameEt.setTextColor(ContextCompat.getColor(context, R.color.lightBlack))
                binding.profileSettingCompleteCv.isClickable = false
                binding.profileSettingCompleteCv.setCardBackgroundColor((ContextCompat.getColor(context, R.color.gray)))}

            override fun afterTextChanged(s: Editable?) {
                return }
        })
    }


    override fun onNicknameSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.profileSettingDuplicationCheckOffTv.visibility = View.GONE
        binding.profileSettingDuplicationCheckOnTv.visibility = View.VISIBLE
        binding.profileSettingPossibleTv.visibility = View.VISIBLE
        binding.profileSettingErrorTv.visibility = View.GONE
        binding.profileSettingCompleteCv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.main))
        binding.profileSettingCompleteCv.isClickable = true
    }

    override fun onNicknameFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            2001 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            2002 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            3001 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
        }
        binding.profileSettingNicknameEt.setTextColor(ContextCompat.getColor(context, R.color.red))
        binding.profileSettingErrorTv.text = message
        binding.profileSettingErrorTv.visibility = View.VISIBLE
        binding.profileSettingPossibleTv.visibility = View.GONE
    }

    override fun onNicknameLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onSignUpSuccess(message: String, auths: Auths) {
        LoadingDialog.loadingOff()
        saveJwt(context ,auths.token.jwt!!)
        saveUserIdx(context, auths.token.userIdx!!)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            2000 -> Toast.makeText(this, "다시 시도해 주세요!", Toast.LENGTH_SHORT).show()
            3000 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            4000 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSignUpLoading() {
        LoadingDialog.loadingOn(this)
    }
}