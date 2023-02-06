package com.tripper.tripper.ui.main.feed.comment
import com.google.gson.annotations.SerializedName

data class CommentResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CommentIdx
        )

data class CommentIdx (
    val commentIdx : Int
        )
