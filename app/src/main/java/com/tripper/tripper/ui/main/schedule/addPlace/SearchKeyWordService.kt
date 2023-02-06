package com.tripper.tripper.ui.main.schedule.addPlace

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchKeyWordService {

    private lateinit var searchKeyWordView : SearchKeyWordView

    fun setSearchKeyWordView(searchKeyWordView: SearchKeyWordView){
        this.searchKeyWordView = searchKeyWordView
    }

    fun searchKeyWord(jwt: String, area: String, page: Int){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val searchKeyWordService = retrofit!!.create(SearchKeyWordInterface::class.java)

        searchKeyWordView.onSearchKeyWordLoading()

        searchKeyWordService.getSearchKeyWord(jwt,area,page).enqueue(object : Callback<SearchKeyWordResponse>{
            override fun onResponse(
                call: Call<SearchKeyWordResponse>,
                searchKeyWordResponse: Response<SearchKeyWordResponse>
            ) {
                val response = searchKeyWordResponse.body()!!
                when(response.code){
                    1013 -> searchKeyWordView.onSearchKeyWordSuccess(response.message, response.result)
                    else -> searchKeyWordView.onSearchKeyWordFailure(response.code, response.message)

                }
            }

            override fun onFailure(call: Call<SearchKeyWordResponse>, t: Throwable) {
                searchKeyWordView.onSearchKeyWordFailure(400, "네트워크 에러")
            }
        })
    }


}