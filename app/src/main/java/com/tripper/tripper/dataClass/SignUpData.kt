package com.tripper.tripper.dataClass

data class SignUpData(
    var email: String,
    var profileImgUrl: String,
    var kakaoId: String,
    var ageGroup: String?,
    var gender: String?,
    var nickName: String

)
