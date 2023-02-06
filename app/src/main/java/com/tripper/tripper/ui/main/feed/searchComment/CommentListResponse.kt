package com.tripper.tripper.ui.main.feed.searchComment

import com.google.gson.annotations.SerializedName
import com.tripper.tripper.ui.main.feed.userProfile.UserProfileResult

data class CommentListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CommentInfo
)

data class CommentInfo(
    var totalCommentCount: Int,
    var comments: ArrayList<Comments>
)

data class Comments(
    var commentIdx: Int,
    var userIdx: Int,
    var nickName: String,
    var profileImgUrl: String,
    var comment: String,
    var commentStatus: String,
    var secondCommentCount: Int?,
    var createdAt: String
)
