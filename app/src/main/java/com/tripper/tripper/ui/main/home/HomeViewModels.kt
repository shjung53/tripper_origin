package com.tripper.tripper.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.ui.main.feed.userProfile.OtherProfileFeed
import com.tripper.tripper.ui.main.home.feedInquiry.FeedData

class LikeViewModel : ViewModel(){
    private val _like = MutableLiveData<Int>()
    val like: LiveData<Int> get() = _like

    fun getLike(like: Int) {
        _like.value = like
    }

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _position

    fun getPosition(position: Int) {
        _position.value = position
    }
}

class UserProfileViewModel : ViewModel(){
    private val _userProfileData = MutableLiveData<ArrayList<OtherProfileFeed>>()
    val userProfileData: LiveData<ArrayList<OtherProfileFeed>> get() = _userProfileData

    fun getData(userProfileData: ArrayList<OtherProfileFeed>){
        _userProfileData.value = userProfileData
    }

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> get() = _page

    fun getPage(page: Int){
        _page.value = page
    }
}

class HomeFeedViewModel: ViewModel(){

    private val _latestPage = MutableLiveData<Int>()
    val latestPage: LiveData<Int> get() = _latestPage

    fun latestPage(latestPage: Int){
        _latestPage.value = latestPage
    }

    private val _hotPage = MutableLiveData<Int>()
    val hotPage: LiveData<Int> get() = _hotPage

    fun hotPage(hotPage: Int){
        _hotPage.value = hotPage
    }

    private val _followPage = MutableLiveData<Int>()
    val followPage: LiveData<Int> get() = _followPage

    fun followPage(followPage: Int){
        _followPage.value = followPage
    }

    private val _latestFeedData = MutableLiveData<ArrayList<FeedData>>()
    val latestFeedData: LiveData<ArrayList<FeedData>> get() = _latestFeedData

    fun latestFeedData(latestFeedData: ArrayList<FeedData>){
        _latestFeedData.value = latestFeedData
    }

    private val _hotFeedData = MutableLiveData<ArrayList<FeedData>>()
    val hotFeedData: LiveData<ArrayList<FeedData>> get() = _hotFeedData

    fun hotFeedData(hotFeedData: ArrayList<FeedData>){
        _hotFeedData.value = hotFeedData
    }

    private val _followFeedData = MutableLiveData<ArrayList<FeedData>>()
    val followFeedData: LiveData<ArrayList<FeedData>> get() = _followFeedData

    fun followFeedData(followFeedData: ArrayList<FeedData>){
        _followFeedData.value = followFeedData
    }

}