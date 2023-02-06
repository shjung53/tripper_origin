package com.tripper.tripper.ui.main.schedule.addPlace
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.dataClass.*
import com.tripper.tripper.databinding.ActivityAddPlaceBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.schedule.ScheduleActivity
import com.tripper.tripper.ui.main.schedule.SearchKeywordViewModel
import gun0912.tedimagepicker.util.ToastUtil


class AddPlaceActivity : AppCompatActivity(), SearchKeyWordView {

    private lateinit var viewBinding: ActivityAddPlaceBinding

    private lateinit var searchKeyWordService: SearchKeyWordService

    private var searchKeyWordData = ArrayList<SearchKeyWordData>()

    private lateinit var addPlaceRVAdapter : AddPlaceRVAdapter

    var review = ArrayList<Review>()
    var area = ArrayList<Area>()
    var pageIndex = 0
    var page: Int = 1
    var click = false
    var savePage = 0

    private val searchKeywordModel: SearchKeywordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityAddPlaceBinding.inflate(layoutInflater)



        val token = getJwt(this)


        searchKeyWordService = SearchKeyWordService()
        searchKeyWordService.setSearchKeyWordView(this)


        val reviewImgKey = intent.getParcelableArrayListExtra<KeyLocation>("reviewImgKey")
        area = intent.getParcelableArrayListExtra("Area")!!
        review = intent.getParcelableArrayListExtra("Review")!!
        pageIndex = intent.getIntExtra("pageIndex",0)
        val myPlace = intent.getParcelableArrayListExtra<MyPlace>("myPlace") as ArrayList<MyPlace>
        val itemPosition = intent.getIntExtra("position", 0)


        viewBinding.addPlaceBackIv.setOnClickListener {
            finish()
        }


        addPlaceRVAdapter = AddPlaceRVAdapter(searchKeyWordData)
        viewBinding.addPlaceRcv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.addPlaceRcv.adapter = addPlaceRVAdapter



        addPlaceRVAdapter.setMyItemClickListener(object : AddPlaceRVAdapter.MyItemClickListener {
            override fun addPlace(holder: AddPlaceRVAdapter.ViewHolder, position: Int) {
                val placeType = when(searchKeyWordData[position].category_code){
                    "FD6" -> 1
                    "CE7" -> 1
                    "AT4" -> 2
                    "AD5" -> 3
                    null -> 4
                    else -> {4}
                }
                val category = if(searchKeyWordData[position].category_name == null){"기타"}else{
                        searchKeyWordData[position].category_name}
                val longitude = searchKeyWordData[position].x
                val latitude = searchKeyWordData[position].y
                val name =holder.binding.addPlaceRvSpaceNameTv.text.toString()
                val address =holder.binding.addPlaceRvSpaceAddressTv.text.toString()
                myPlace.add(itemPosition, MyPlace(placeType, name, address))
                review.apply {
                    add(itemPosition,Review(null,""))}
                area.apply {
                    add(itemPosition,Area(category,latitude,longitude,name,address,null))
                }
                reviewImgKey?.add(itemPosition,KeyLocation(null))
                val intent = Intent(this@AddPlaceActivity, ScheduleActivity::class.java).apply {
                    putExtra("myPlace",myPlace)
                    putExtra("itemPosition",itemPosition)
                    putExtra("Area",area)
                    putExtra("Review",review)
                    putExtra("pageIndex",pageIndex)
                    putExtra("reviewImgKey",reviewImgKey)
                }
                setResult(1,intent)
                finish()
            }
        }
        )


        viewBinding.addPlaceSearchPlaceIc.setOnClickListener {
            val keyWord: String = viewBinding.addPlaceEt.text.toString()
            click = true
            page = 1
            searchKeywordModel.getPage(1)
            searchKeyWordService.searchKeyWord(token, keyWord, page)

        }



            viewBinding.addPlaceRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val keyWord: String = viewBinding.addPlaceEt.text.toString()
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    searchKeyWordData = searchKeywordModel.searchKeyWordData.value!!
                    page = searchKeywordModel.page.value!!
                    val lastItemPosition = searchKeyWordData.size - 1
                    if (lastVisibleItemPosition == lastItemPosition) {
                        if (savePage == page) {
                            Toast.makeText(ToastUtil.context, "검색결과가 더이상 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            searchKeyWordService.searchKeyWord(token, keyWord, page)
                            savePage = page
                        }
                    }
                }
            })




        setContentView(viewBinding.root)

    }


    override fun onSearchKeyWordSuccess(message: String, result: PlaceResult) {
        LoadingDialog.loadingOff()
        if(click){
            searchKeyWordData.clear()
            searchKeywordModel.getData(searchKeyWordData)
            addPlaceRVAdapter.resetPlace(searchKeyWordData)
        }
        click = false
        searchKeyWordData.apply {
            for(i in 1 .. result.list.size){
            add(result.list[i-1])
            }
        }
        searchKeywordModel.getData(searchKeyWordData)
        searchKeywordModel.getPage(page+1)
        Toast.makeText(this, "장소 검색 결과 조회", Toast.LENGTH_SHORT).show()
        addPlaceRVAdapter.resetPlace(searchKeyWordData)
        addPlaceRVAdapter.addPlace(searchKeyWordData, searchKeyWordData.size-1)
    }

    override fun onSearchKeyWordFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            3008 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            2021 ->  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            4000,4001 -> Toast.makeText(this, "서버오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSearchKeyWordLoading() {
        if(!LoadingDialog.loadingDialog.isShowing){
            LoadingDialog.loadingOn(this)}
    }
}


