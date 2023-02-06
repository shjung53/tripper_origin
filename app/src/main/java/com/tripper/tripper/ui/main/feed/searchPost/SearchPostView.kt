package com.tripper.tripper.ui.main.feed.searchPost

interface SearchPostView {
    fun onSearchPostSuccess(message: String, result: SearchPostResult)
    fun onSearchPostFailure(code: Int, message: String)
    fun onSearchPostLoading()
}

