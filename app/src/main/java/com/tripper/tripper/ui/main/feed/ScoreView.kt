package com.tripper.tripper.ui.main.feed

interface ScoreView {
    fun onScoreSuccess(message: String)
    fun onScoreFailure(code: Int, message: String)
    fun onScoreLoading()
}