package com.tripper.tripper.ui.main.feed.reply

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Response

class ReplyService {

    private lateinit var replyView: ReplyView

    fun setReplyView(replyView: ReplyView){
        this.replyView = replyView
    }

    fun getReply(jwtToken: String, feedIdx: Int, commentIdx: Int, page: Int){

        val retrofit = Retrofit.getRetrofit()

        val postReplyService = retrofit!!.create(ReplyInterface::class.java)

        replyView.onReplyLoading()

        postReplyService.getReply(jwtToken, feedIdx, commentIdx, page).enqueue(object : retrofit2.Callback<ReplyResponse> {
            override fun onResponse(call: Call<ReplyResponse>, replyResponse: Response<ReplyResponse>) {
                val response = replyResponse.body()!!

                when(response.code){
                    1038 -> replyView.onReplySuccess(response.code, response.message, response.result)
                    else -> replyView.onReplyFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ReplyResponse>, t: Throwable) {
                replyView.onReplyFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}