package com.tripper.tripper.ui.setting

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditService {

    private lateinit var profileEditView: ProfileEditView

    fun setProfileEditView(profileEditView: ProfileEditView){
        this.profileEditView = profileEditView
    }

    fun profileEdit(xAccessToken: String, profileImage: MultipartBody.Part?, nickName: HashMap<String, RequestBody>){

        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val profileEditService = retrofit!!.create(ProfileEditInterface::class.java)

        profileEditView.onProfileEditLoading()

        profileEditService.profileEdit(xAccessToken, profileImage, nickName).enqueue(object : Callback<ProfileEditResponse>{
            override fun onResponse(call: Call<ProfileEditResponse>, profileEditResponse: Response<ProfileEditResponse>) {
                val response = profileEditResponse.body()!!
                when(response.code){
                    1014, 3009 -> profileEditView.onProfileEditSuccess(response.message)
                    else -> profileEditView.onProfileEditFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ProfileEditResponse>, t: Throwable) {
                profileEditView.onProfileEditFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}