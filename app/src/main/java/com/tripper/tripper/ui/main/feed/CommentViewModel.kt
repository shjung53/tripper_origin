package com.tripper.tripper.ui.main.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.ui.main.feed.searchComment.Comments

class CommentViewModel: ViewModel() {
    private val _commentData = MutableLiveData<ArrayList<Comments>>()
    val commentData: LiveData<ArrayList<Comments>> get() = _commentData

    fun getCommentData(commentData: ArrayList<Comments>){
        _commentData.value = commentData
    }

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> get() = _page

    fun getPage(page: Int){
        _page.value = page
    }

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _position

    fun getPosition(position: Int){
        _position.value = position
    }
}