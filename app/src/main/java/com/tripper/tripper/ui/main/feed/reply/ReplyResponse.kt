package com.tripper.tripper.ui.main.feed.reply

import com.google.gson.annotations.SerializedName

data class ReplyResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<Reply>
        )

data class Reply (
    val commentIdx: Int,
    val userIdx: Int,
    val nickName: String,
    val profileImgUrl: String,
    val comment: String,
    val createdAt: String
        )

