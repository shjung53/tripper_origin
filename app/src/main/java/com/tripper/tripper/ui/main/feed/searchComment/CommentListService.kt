package com.tripper.tripper.ui.main.feed.searchComment

import CommentListView
import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentListService {
    private lateinit var commentListView: CommentListView

    fun setCommentListView(commentListView: CommentListView){
        this.commentListView = commentListView
    }

    fun getCommentList(jwt: String, feedIdx: Int, page: Int){
        val retrofit = Retrofit.getRetrofit()

        val commentListService = retrofit!!.create(CommentListInterface::class.java)
        commentListView.onCommentListLoading()

        commentListService.getCommentList(jwt, feedIdx, page).enqueue(object :
            Callback<CommentListResponse>{
            override fun onResponse(
                call: Call<CommentListResponse>,
                commentListResponse: Response<CommentListResponse>
            ) {
                val response = commentListResponse.body()!!
                when(response.code){
                    1031 -> commentListView.onCommentListSuccess(response.message, response.result)
                    else -> commentListView.onCommentListFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                commentListView.onCommentListFailure(400,"네트워크 에러")
            }

        })

    }
}