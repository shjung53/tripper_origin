package com.tripper.tripper.ui.main.schedule.postFeed

import com.tripper.tripper.ui.main.feed.TravelIdx

interface PostFeedView {

    fun onPostSuccess(code: Int, message: String, result: TravelIdx)
    fun onPostFailure(code: Int, message: String)
    fun onPostLoading()
}