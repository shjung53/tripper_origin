package com.tripper.tripper.ui.main.feed.comment

import com.tripper.tripper.Retrofit
import com.tripper.tripper.ui.main.schedule.postFeed.PostFeedService
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class CommentService {

    private lateinit var commentView: CommentView

    fun setCommentView(commentView: CommentView){
        this.commentView = commentView
    }


    fun postComment(jwtToken: String, commentData: CommentData){

        val retrofit = Retrofit.getRetrofit()

        val postCommentService = retrofit!!.create(CommentInterface::class.java)

        commentView.onCommentLoading()

        postCommentService.postComment(jwtToken, commentData).enqueue(object : retrofit2.Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, commentResponse: Response<CommentResponse>) {
                val response = commentResponse.body()!!

                when(response.code){
                    1026 -> commentView.onCommentSuccess(response.code, response.message, response.result)
                    else -> commentView.onCommentFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                commentView.onCommentFailure(400, "네트워크 오류가 발생했습니다")
            }

        })
}}