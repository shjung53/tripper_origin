package com.tripper.tripper
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.tripper.tripper.ui.login.AccessToken
import com.tripper.tripper.ui.login.Auth
import com.tripper.tripper.ui.login.Auths
import com.tripper.tripper.ui.main.schedule.addPlace.SearchKeyWordData

    fun saveJwt(context: Context, jwt: String){
        val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    fun saveUserIdx(context: Context, userIdx: Int){
        val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("userIdx", userIdx)
        editor.apply()
    }


    fun saveSignUpInfo(context: Context, auth: Auth){
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("email", auth.email)
        editor.putString("profileImgUrl", auth.profileImgUrl)
        editor.putString("kakaoId", auth.kakaoId)
        editor.putString("ageGroup", auth.ageGroup)
        editor.putString("gender", auth.gender)
        editor.apply()
    }



    fun getEmail(context: Context): String{
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)

        return spf.getString("email","").toString()
    }
    fun getProfileImgUrl(context: Context): String{
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)

        return spf.getString("profileImgUrl","").toString()
    }
    fun getKakaoId(context: Context): String{
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)

     return spf.getString("kakaoId","").toString()
    }
    fun getAgeGroup(context: Context): String{
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)

        return spf.getString("ageGroup","").toString()
    }
    fun getGender(context: Context): String{
        val spf = context.getSharedPreferences("signUp", AppCompatActivity.MODE_PRIVATE)

        return spf.getString("gender","").toString()
    }


    fun getUserIdx(context: Context): Int {
        val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf.getInt("userIdx",0)
    }

    fun getJwt(context: Context): String {
        val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf.getString("jwt", "")!!
    }

