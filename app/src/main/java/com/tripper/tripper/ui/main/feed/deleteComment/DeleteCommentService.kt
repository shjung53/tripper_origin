package com.tripper.tripper.ui.main.feed.deleteComment

import android.util.Log
import com.tripper.tripper.ui.main.myInfo.DeleteFeedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteCommentService {

    private lateinit var deleteCommentView: DeleteCommentView

    fun setDeleteCommentView(deleteCommentView: DeleteCommentView){
        this.deleteCommentView = deleteCommentView
    }

    fun deleteComment(token: String, feedIdx: Int, commentIdx: Int){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val deleteCommentService = retrofit!!.create(DeleteCommentInterface::class.java)
        deleteCommentView.onDeleteCommentLoading()

        deleteCommentService.deleteComment(token, feedIdx, commentIdx).enqueue(object : Callback<DeleteCommentResponse> {
            override fun onResponse(call: Call<DeleteCommentResponse>, deleteCommentResponse: Response<DeleteCommentResponse>) {
                val response = deleteCommentResponse.body()!!
                when(response.code){
                    1037 -> deleteCommentView.onDeleteCommentSuccess(response.message)
                    else -> deleteCommentView.onDeleteCommentFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {
                deleteCommentView.onDeleteCommentFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}