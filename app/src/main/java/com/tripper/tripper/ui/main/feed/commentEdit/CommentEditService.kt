package com.tripper.tripper.ui.main.feed.commentEdit

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentEditService {
    private lateinit var commentEditView: CommentEditView

    fun setCommentEditView(commentEditView: CommentEditView){
        this.commentEditView = commentEditView
    }

    fun commentEdit(jwt:String, feedIdx: Int, commentIdx:Int, comment: Comment){
        val retrofit = Retrofit.getRetrofit()

        val commentEditService = retrofit!!.create(CommentEditInterface::class.java)

        commentEditView.onCommentEditLoading()

        commentEditService.commentEdit(jwt, feedIdx, commentIdx, comment).enqueue(object :
            Callback<CommentEditResponse>{
            override fun onResponse(
                call: Call<CommentEditResponse>,
                commentEditResponse: Response<CommentEditResponse>
            ) {
                val response = commentEditResponse.body()!!
                when(response.code){
                    1027 -> commentEditView.onCommentEditSuccess(response.message)
                    else -> commentEditView.onCommentEditFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<CommentEditResponse>, t: Throwable) {
                commentEditView.onCommentEditFailure(400,"네트워크 에러")
            }
        })
    }
}