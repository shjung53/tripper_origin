package com.tripper.tripper.ui.main.feed.reply

interface ReplyView {
    fun onReplySuccess(code: Int, message: String, result: ArrayList<Reply>)
    fun onReplyFailure(code: Int, message: String)
    fun onReplyLoading()
}