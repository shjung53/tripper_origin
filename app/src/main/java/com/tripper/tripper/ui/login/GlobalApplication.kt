package com.tripper.tripper.ui.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "8b4db2cdbe569d851ed5355957ac66d9")
    }
}