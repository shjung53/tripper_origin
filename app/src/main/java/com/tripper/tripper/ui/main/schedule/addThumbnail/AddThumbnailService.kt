package com.tripper.tripper.ui.main.schedule.addThumbnail
import android.util.Log
import com.tripper.tripper.Retrofit
import okhttp3.MultipartBody
import retrofit2.*

class AddThumbnailService {

    private lateinit var addThumbnailView: AddThumbnailView

    fun setAddThumbnailView(addThumbnailView: AddThumbnailView){
        this.addThumbnailView = addThumbnailView
    }


    fun addThumbnail(xAccessToken: String, thumnails: ArrayList<MultipartBody.Part>?){
        val retrofit = Retrofit.getRetrofit()

        val addThumbnailService = retrofit!!.create(AddThumbnailInterface::class.java)
        addThumbnailView.onAddThumbnailLoading()

        addThumbnailService.addThumbnails(xAccessToken, thumnails).enqueue(object : Callback<AddThumbnailResponse>{
            override fun onResponse(call: Call<AddThumbnailResponse>, addThumbnailResponse: Response<AddThumbnailResponse>) {
                val response = addThumbnailResponse.body()!!
                when(response.code){
                    1015 -> addThumbnailView.onAddThumbnailSuccess(response.message, response.result)
                    else -> addThumbnailView.onAddThumbnailFailure(response.code, response.message)
                }
            }

            override fun onFailure(call: Call<AddThumbnailResponse>, t: Throwable) {
                addThumbnailView.onAddThumbnailFailure(400,"네트워크 오류가 발생했습니다")
            }
        })



    }
}