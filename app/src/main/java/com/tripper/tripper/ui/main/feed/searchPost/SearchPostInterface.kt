package com.tripper.tripper.ui.main.feed.searchPost

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SearchPostInterface {
    @GET("/app/feeds/{feedIdx}/search")
    fun getSearchPost(
        @Header ("x-access-token") jwt: String,
        @Path ("feedIdx") feedIdx:Int
    ): Call<SearchFeedResponse>
}

