package com.tripper.tripper.ui.setting
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tripper.tripper.databinding.ActivitySettingBinding
import com.tripper.tripper.ui.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class SettingActivity: AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBackIv.setOnClickListener {
            onBackPressed()
        }

        binding.settingAccountCl.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        binding.settingLogoutCl.setOnClickListener {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                    }else {
                        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
                        val editor = spf.edit()
                        editor.remove("jwt")
                        editor.apply()
                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
        }



    }


}