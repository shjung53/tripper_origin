package com.tripper.tripper.dataClass

data class PostListMultiData(
    val placeType: Int = 0,
    var category: String = "기타",
    val name: String,
    val reviewImgList: ArrayList<ReviewImgData>,
    val reviewPost: String?,
    var isExpanded: Boolean = false
)
