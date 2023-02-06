package com.tripper.tripper.ui.main.feed

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoreService {

    private lateinit var scoreView: ScoreView

    fun setScoreView(scoreView: ScoreView){
        this.scoreView = scoreView
    }

    fun score(token: String, scoreData: ScoreData){
        val retrofit = com.tripper.tripper.Retrofit.getRetrofit()

        val scoreService = retrofit!!.create(ScoreInterface::class.java)

        scoreView.onScoreLoading()

        scoreService.score(token, scoreData).enqueue(object : Callback<ScoreResponse> {
            override fun onResponse(call: Call<ScoreResponse>, scoreResponse: Response<ScoreResponse>) {
                val response = scoreResponse.body()!!
                when(response.code) {
                    1020, 1021 -> scoreView.onScoreSuccess(response.message)
                    else -> scoreView.onScoreFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<ScoreResponse>, t: Throwable) {
                scoreView.onScoreFailure(400, "네트워크 오류가 발생했습니다")
            }
        })
    }
}