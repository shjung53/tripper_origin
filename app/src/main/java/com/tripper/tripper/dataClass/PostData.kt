package com.tripper.tripper.dataClass
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PostData(
    var information : Information,
    var day : ArrayList<Day>,
    var metadata: MetaData?
)

data class Information(
    var startDate: String,
    var endDate: String,
    var traffic: String,
    var title: String,
    var introduce: String
)

//데이
data class Day(
    var area: ArrayList<Area>?
)

@Parcelize
data class Area (
    var category: String?,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var address: String?,
    var review: Review?
    ):Parcelable

@Parcelize
data class Review(
    var images: ArrayList<String>?,
    var comment: String?
):Parcelable


data class MetaData(
    var hashtag: ArrayList<String>?,
    var thumnails: ArrayList<String>?
)

@Parcelize
data class ReviewImgKey(
    var reviewImgKey: ArrayList<KeyLocation>?
):Parcelable

@Parcelize
data class KeyLocation(
    var keyLocation: ArrayList<ImgKey>?
):Parcelable

@Parcelize
data class ImgKey (
    var key: String?,
    var uri: Uri?
):Parcelable

data class PostDayData(
    var placeType: Int = 0,
    var areaIdx: Int,
    var dayIdx: Int,
    var areaCategory: String,
    var areaName: String,
    var areaLatitude: Double,
    var areaLongitude: Double
)

data class AreaLocation(
    var latitude: Double,
    var longitude: Double
)

data class ScheduleData(
    var dayData: ArrayList<PostDayData>
)





