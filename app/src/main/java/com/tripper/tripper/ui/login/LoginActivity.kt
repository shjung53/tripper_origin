package com.tripper.tripper.ui.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManagerProvider
import com.tripper.tripper.*
import com.tripper.tripper.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.tripper.tripper.ui.splash.AutoLoginService
import com.tripper.tripper.ui.splash.AutoLoginView
import gun0912.tedimagepicker.util.ToastUtil.context

class LoginActivity  : AppCompatActivity(), LoginView, AutoLoginView {

    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var autoLoginService: AutoLoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val keyHash = Utility.getKeyHash(this)

        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

//        카카오 로그인

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
            }
            else if (tokenInfo != null) {

                if (AuthApiClient.instance.hasToken()) {
                    UserApiClient.instance.accessTokenInfo { _, error ->
                        if (error != null) {
                            Toast.makeText(this, "로그인 정보가 만료되었습니다", Toast.LENGTH_SHORT).show()
                            //로그인 필요
                        }
                        else {
//                            토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                            autoLoginService = AutoLoginService()
                            autoLoginService.setAutoLoginView(this)
                            val token = getJwt(this)
                            autoLoginService.autoLogin(token)
                        }
                    }
                }
                else {
                    Toast.makeText(this, "로그인 정보가 만료되었습니다", Toast.LENGTH_SHORT).show()
                    //로그인 필요
                }
            }
        }


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                val loginToken = token.accessToken
                val accessToken = AccessToken(loginToken)
                val authService = AuthService()
                authService.setLoginView(this)
                authService.login(accessToken)
            }
        }




        viewBinding.loginKakaoBtn.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }


    }

    override fun onLoginSuccess(auth: Auth) {
        LoadingDialog.loadingOff()
        saveJwt(context ,auth.jwt!!)
        saveUserIdx(context, auth.userIdx!!)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when(code){
            2050,2051 -> Toast.makeText(this, "다시 시도해 주세요!", Toast.LENGTH_SHORT).show()
            4000,4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSignUp(message: String, auth: Auth) {
        LoadingDialog.loadingOff()
        saveSignUpInfo(context, auth)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ProfileSettingActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onAutoLoginSuccess(message: String) {
        LoadingDialog.loadingOff()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAutoLoginLoading() {
        LoadingDialog.loadingOn(this)
    }
}