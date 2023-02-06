package com.tripper.tripper.ui.main.feed.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.dataClass.PostDayData
import com.tripper.tripper.dataClass.ScheduleData
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewData

class PostViewModels : ViewModel() {
    private val _dateIdx = MutableLiveData<Int>()
    val dateIdx: LiveData<Int> get() = _dateIdx

    fun getDateIdx(dateIdx: Int) {
        _dateIdx.value = dateIdx
    }

    private val _feedIdx = MutableLiveData<Int>()
    val feedIdx: LiveData<Int> get() = _feedIdx

    fun getFeedIdx(feedIdx: Int) {
        _feedIdx.value = feedIdx
    }

    private val _dayIdx = MutableLiveData<ArrayList<Int>>()
    val dayIdx: LiveData<ArrayList<Int>> get() = _dayIdx

    fun getDayIdx(dayIdx: ArrayList<Int>) {
        _dayIdx.value = dayIdx
    }

    private val _dayData = MutableLiveData<ArrayList<PostDayData>>()
    val dayData : LiveData<ArrayList<PostDayData>> get() = _dayData

    fun getDayData(dayData: ArrayList<PostDayData>) {
        _dayData.value = dayData
    }

    private val _scheduleData = MutableLiveData<ArrayList<ScheduleData>>()
    val scheduleData : LiveData<ArrayList<ScheduleData>> get() = _scheduleData

    fun getScheduleData(scheduleData: ArrayList<ScheduleData>) {
        _scheduleData.value = scheduleData
    }
}