package com.tripper.tripper

import android.app.Application

class ApplicationClass: Application() {

    companion object{
        const val DEV_SERVER_URL = "https://dev.hellotripper.shop/"
        const val PROD_SERVER_URL = "https://prod.hellotripper.shop/"
    }
}
//레트로핏에 URL 넣을 때 아래와 같이 넣어야함!
//Application.DEV_SERVER_URL
//ApplicationClass.DEV_SERVER_URL
//.baseUrl(ApplicationClass.DEV_SERVER_URL)