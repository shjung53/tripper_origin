package com.tripper.tripper.ui.setting

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileEditInterface {
    @Multipart
    @PATCH("/app/users/profile-edit")
    fun profileEdit(
        @Header("x-access-token") xAccessToken: String,
        @Part profileImage: MultipartBody.Part?,
        @PartMap nickName: HashMap<String, RequestBody>
        ): Call<ProfileEditResponse>
}