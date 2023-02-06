package com.tripper.tripper.ui.main.schedule.addThumbnail

interface AddThumbnailView {
    fun onAddThumbnailSuccess(message: String, result: ArrayList<ThumbnailInfo>)
    fun onAddThumbnailFailure(code: Int, message: String)
    fun onAddThumbnailLoading()
}