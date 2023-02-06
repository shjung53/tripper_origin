package com.tripper.tripper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    private var retrofit: Retrofit? = null

//    private const val baseUrl = "https://dev.hellotripper.shop"

    fun getRetrofit(): Retrofit? {
        if (retrofit == null){
            retrofit = Retrofit.Builder().baseUrl("https://dev.hellojason.shop")
                .addConverterFactory(GsonConverterFactory.create()).build()


        }
        return retrofit
    }

}