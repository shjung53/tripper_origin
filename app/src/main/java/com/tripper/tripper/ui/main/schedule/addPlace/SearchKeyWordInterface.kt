package com.tripper.tripper.ui.main.schedule.addPlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchKeyWordInterface {
    @GET("/app/feeds/area-search-keyword")
    fun getSearchKeyWord(
     @Header("x-access-token") jwt:String,
     @Query("area") area: String,
     @Query("page") page: Int
    ): Call<SearchKeyWordResponse>

}