package com.tripper.tripper.dataClass

data class MapFeedData(
    var picture: Int = 0,
    var profileImg: Int = 0,
    var nickname: String? = "",
    var title: String = "",
    var hashtag: String? = "",
    var rate: Int = 0,
    var people: Int = 0
)