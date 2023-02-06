package com.tripper.tripper.ui.main.schedule.addPlace

interface SearchKeyWordView {

    fun onSearchKeyWordSuccess(message:String, result: PlaceResult)
    fun onSearchKeyWordFailure(code: Int, message: String)
    fun onSearchKeyWordLoading()
}