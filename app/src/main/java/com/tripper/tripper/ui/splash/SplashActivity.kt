package com.tripper.tripper.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.MainActivity
import com.tripper.tripper.databinding.ActivitySplashBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
         val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            }, 2000
        )
    }

}