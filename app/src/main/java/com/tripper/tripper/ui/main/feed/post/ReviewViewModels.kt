package com.tripper.tripper.ui.main.feed.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewData

class ReviewViewModels: ViewModel() {

    private val _reviewPos = MutableLiveData<Int>()
    val reviewPos: LiveData<Int> get() = _reviewPos

    fun getReviewPos(reviewPos: Int) {
        _reviewPos.value = reviewPos
    }

    private val _allReview = MutableLiveData<ArrayList<ReviewData>>()
    val allReview : LiveData<ArrayList<ReviewData>> get() = _allReview

    fun getAllReview(allReview: ArrayList<ReviewData>) {
        _allReview.value = allReview
    }

    private val _signalReview = MutableLiveData<Int>()
    val signalReview : LiveData<Int> get() = _signalReview

    fun getSignalReview(signalReview: Int) {
        _signalReview.value = signalReview
    }
}