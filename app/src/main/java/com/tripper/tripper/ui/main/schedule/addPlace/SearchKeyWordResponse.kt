package com.tripper.tripper.ui.main.schedule.addPlace
import com.google.gson.annotations.SerializedName

data class SearchKeyWordResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PlaceResult
)


data class PlaceResult  (
    var pageNum: Int,
    var isEnd: Boolean,
    var list: ArrayList<SearchKeyWordData>
)

data class SearchKeyWordData(
    var address_name:String,
    var category_code: String?,
    var category_name: String?,
    var place_name: String,
    var x: Double,
    var y: Double
)

