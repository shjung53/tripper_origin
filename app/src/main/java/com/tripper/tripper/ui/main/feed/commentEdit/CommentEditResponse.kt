package com.tripper.tripper.ui.main.feed.commentEdit

import com.google.gson.annotations.SerializedName

data class CommentEditResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
