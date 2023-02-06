package com.tripper.tripper.ui.main.schedule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tripper.tripper.dataClass.Day
import com.tripper.tripper.dataClass.ReviewImgKey
import com.tripper.tripper.ui.main.schedule.addPlace.SearchKeyWordData

class DayIndexViewModel : ViewModel(){
    private val _dayIndex = MutableLiveData<Int>()
    val dayIndex: LiveData<Int> get() = _dayIndex

    fun getDayIndex(dateIndex: Int) {
        _dayIndex.value = dateIndex
    }

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> get() = _startDate

    fun getStartDate(startDate: String) {
        _startDate.value = startDate
    }

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> get() = _endDate

    fun getEndDate(endDate: String) {
        _endDate.value = endDate
    }

}

class DayViewModel : ViewModel(){
    private val _day = MutableLiveData<ArrayList<Day>>()
    val day: LiveData<ArrayList<Day>> get() = _day

    fun getDay(day: ArrayList<Day>){
        _day.value = day
    }
}

class ReviewImgKeyModel : ViewModel(){
    private val _allReviewImgKey = MutableLiveData<ArrayList<ReviewImgKey>>()
    val allReviewImgKey: LiveData<ArrayList<ReviewImgKey>> get() = _allReviewImgKey

    fun getKey(allReviewImgKey: ArrayList<ReviewImgKey>){
        _allReviewImgKey.value = allReviewImgKey
    }

}

class SearchKeywordViewModel : ViewModel(){
    private val _searchKeyWordData = MutableLiveData<ArrayList<SearchKeyWordData>>()
    val searchKeyWordData: LiveData<ArrayList<SearchKeyWordData>> get() = _searchKeyWordData

    fun getData(searchKeyWordData: ArrayList<SearchKeyWordData>){
        _searchKeyWordData.value = searchKeyWordData
    }

    private val _page = MutableLiveData<Int>()
    val page : LiveData<Int> get() = _page

    fun getPage(page: Int){
        _page.value = page
    }
}
