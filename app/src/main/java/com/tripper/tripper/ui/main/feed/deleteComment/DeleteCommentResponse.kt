package com.tripper.tripper.ui.main.feed.deleteComment

import com.google.gson.annotations.SerializedName

class DeleteCommentResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
