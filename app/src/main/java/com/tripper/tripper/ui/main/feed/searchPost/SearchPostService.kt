package com.tripper.tripper.ui.main.feed.searchPost

import android.util.Log
import com.tripper.tripper.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPostService {
    private lateinit var searchPostView: SearchPostView

    fun getSearchPostView(searchFeedView: SearchPostView){
        this.searchPostView = searchFeedView
    }

    fun searchPost(token: String, feedIdx: Int){
        val retrofit = Retrofit.getRetrofit()

        val searchPostService = retrofit!!.create(SearchPostInterface::class.java)

        searchPostView.onSearchPostLoading()

        searchPostService.getSearchPost(token, feedIdx).enqueue(object :
            Callback<SearchFeedResponse>{
            override fun onResponse(
                call: Call<SearchFeedResponse>,
                searchFeedResponse: Response<SearchFeedResponse>
            ) {
                val response = searchFeedResponse.body()!!
                when(response.code){
                    1030 -> searchPostView.onSearchPostSuccess(response.message, response.result)
                    else -> searchPostView.onSearchPostFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<SearchFeedResponse>, t: Throwable) {
                t.printStackTrace()
                searchPostView.onSearchPostFailure(400, "네트워크 에러")
            }
        })
    }
}