package com.tripper.tripper.ui.main.myInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageFeedByOption

class MyTripViewModel: ViewModel() {
    private val _myTripData = MutableLiveData<ArrayList<UserMyPageFeedByOption>>()
    val myTripData: LiveData<ArrayList<UserMyPageFeedByOption>>get() = _myTripData

    fun getData(myTripData: ArrayList<UserMyPageFeedByOption>){
        _myTripData.value = myTripData
    }

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> get() = _page

    fun getPage(page:Int){
        _page.value = page
    }

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _position

    fun getPosition(position: Int){
        _position.value = position
    }

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    fun getStatus(status: String){
        _status.value = status
    }

    private val _likedData = MutableLiveData<ArrayList<UserMyPageFeedByOption>>()
    val likedData: LiveData<ArrayList<UserMyPageFeedByOption>>get() = _likedData

    fun getLikedData(likedData: ArrayList<UserMyPageFeedByOption>){
        _likedData.value = likedData
    }


}


