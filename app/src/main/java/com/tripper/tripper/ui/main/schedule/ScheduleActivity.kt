package com.tripper.tripper.ui.main.schedule
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.tripper.tripper.*
import com.tripper.tripper.dataClass.*
import com.tripper.tripper.databinding.ActivityScheduleBinding
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.feed.TravelIdx
import com.tripper.tripper.ui.main.schedule.addThumbnail.AddThumbnailService
import com.tripper.tripper.ui.main.schedule.addThumbnail.AddThumbnailView
import com.tripper.tripper.ui.main.schedule.addThumbnail.ThumbnailInfo
import com.tripper.tripper.ui.main.schedule.postFeed.PostFeedService
import com.tripper.tripper.ui.main.schedule.postFeed.PostFeedView
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import gun0912.tedimagepicker.util.ToastUtil.context
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import kotlin.collections.ArrayList


class ScheduleActivity: AppCompatActivity(), PostFeedView, AddThumbnailView {

    private val spf = context.getSharedPreferences("days", MODE_PRIVATE)

    lateinit var binding: ActivityScheduleBinding

    var list = ArrayList<Uri>()

    val adapter = ScheduleThumbnailRVAdapter(list,this)

    private var days = spf.getLong("days",0)

    private var dates = ArrayList<DatesData>()

    private var transIndex = 0

    private var dateIndex = 0

    private lateinit var information : Information

    var review = ArrayList<Review>()

    private var day = ArrayList<Day>()

    private var metadata : MetaData? = null

    private var bitmapMultipartBody = ArrayList<MultipartBody.Part>()

    private lateinit var addThumbnailService: AddThumbnailService

    private val dayIndexViewModel: DayIndexViewModel by viewModels()
    private val dayViewModel: DayViewModel by viewModels()
    private val reviewImgKeyModel: ReviewImgKeyModel by viewModels()

    private var allReviewImgKey = ArrayList<ReviewImgKey>()

    private var thumnails = ArrayList<String>()

    private var thumbnailCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        dates = intent.getParcelableArrayListExtra("dates")!!
        val startDate = intent.getStringExtra("startDate").toString()
        val endDate = intent.getStringExtra("endDate").toString()
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transIndex = intent.getIntExtra("transIndex",0)
        val introduceEt = binding.scheduleFeedIntroduceEt
        val titleEt = binding.scheduleFeedTitleEt
        val xAccessToken = getJwt(this)
        addThumbnailService = AddThumbnailService()
        addThumbnailService.setAddThumbnailView(this)

        dayIndexViewModel.getStartDate(startDate)
        dayIndexViewModel.getEndDate(endDate)

//        데이만큼 빈 일정 넣어주기
        for(i in 1..days){
            day.apply {
                add(Day(null))
            }
        }

        for(i in 1..days){
            allReviewImgKey.apply {
                add(ReviewImgKey(null))
            }
        }

        reviewImgKeyModel.getKey(allReviewImgKey)
        dayViewModel.getDay(day)


//        뷰 세팅
        binding.scheduleDateTv.text = "$startDate~$endDate"
        binding.scheduleTransportIv.setImageResource(
            when (transIndex){
                0 -> R.drawable.ic_car_19dp
                1 -> R.drawable.ic_train_14dp
                2 -> R.drawable.ic_bicycle_25dp
                3 -> R.drawable.ic_walk_10dp
                else -> {return}
            })
        binding.scheduleTransportTv.text = when(transIndex){
            0-> "자차로 여행"
            1-> "대중교통 여행"
            2-> "자전거 여행"
            3-> "도보로 여행"
            else -> {return}
        }.toString()



        initClickListener()
        addThumbnail()
        activateCompleteBtn(introduceEt, titleEt)




        val listAdapter = ScheduleListAdapter(this)

        val viewPager = binding.scheduleListVp

        viewPager.adapter = listAdapter


        val scheduleDateRVAdapter = ScheduleDateRVAdapter(this, dates, dateIndex)

        binding.scheduleDateRv.adapter = scheduleDateRVAdapter

        binding.scheduleDateRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



//        해시태그
        binding.scheduleFeedHashtagEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                    val formattedText = ("#" + s.toString())
                        .replaceFirst("##", "#")
                        .replace(Regex(" (\\S)"), " #")
                    if (s.toString() != formattedText) {
                        s?.replace(0, s.length, formattedText, 0, formattedText.length)
                    }
            }
        })

        // 썸네일 이미지 삭제하면 숫자 바꾸기
        adapter.setMyItemClickListener(object: ScheduleThumbnailRVAdapter.MyItemClickListener{
            override fun getSize(size: String) {
                binding.scheduleThumbnailNumTv.text = "$size/5"
            }
        })


//        날짜 버튼과 뷰페이저 동기화
        scheduleDateRVAdapter.setMyItemClickListener(object: ScheduleDateRVAdapter.MyItemClickListener{
            override fun movePage(dateIndex: Int) {
                scheduleDateRVAdapter.notifyItemChanged(dateIndex)
                viewPager.setCurrentItem(dateIndex, true)
                scheduleDateRVAdapter.notifyItemChanged(dateIndex)
                dayIndexViewModel.getDayIndex(dateIndex)
            }

            override fun selectMove() {
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        scheduleDateRVAdapter.notifyItemChanged(scheduleDateRVAdapter.dateIndex)
                        scheduleDateRVAdapter.dateIndex = viewPager.currentItem
                        scheduleDateRVAdapter.notifyItemChanged(scheduleDateRVAdapter.dateIndex)
                        dayIndexViewModel.getDayIndex(viewPager.currentItem)
                    } })
            }
        })


        //      게시물 작성
        binding.scheduleCompleteTv.setOnClickListener {
            if(thumbnailCount > 0){
                addThumbnailService.addThumbnail(xAccessToken,bitmapMultipartBody)}
            else if(thumbnailCount == 0){
                Toast.makeText(this,"썸네일을 한장 이상 등록해 주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }












    private fun activateCompleteBtn(introduce: EditText, title: EditText) {
        introduce.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (title.length() > 0 && introduce.length() > 0) {
                    binding.scheduleCompleteTv.setTextColor(
                        ContextCompat.getColor(
                            this@ScheduleActivity,
                            R.color.main
                        )
                    )
                    binding.scheduleCompleteTv.isEnabled = true
                } else {
                    binding.scheduleCompleteTv.setTextColor(
                        ContextCompat.getColor(
                            this@ScheduleActivity,
                            R.color.gray
                        )
                    )
                    binding.scheduleCompleteTv.isEnabled = false
                }
            }
        })

        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (title.length() > 0 && introduce.length() > 0) {
                    binding.scheduleCompleteTv.setTextColor(
                        ContextCompat.getColor(
                            this@ScheduleActivity,
                            R.color.main
                        )
                    )
                    binding.scheduleCompleteTv.isEnabled = true
                } else {
                    binding.scheduleCompleteTv.setTextColor(
                        ContextCompat.getColor(
                            this@ScheduleActivity,
                            R.color.gray
                        )
                    )
                    binding.scheduleCompleteTv.isEnabled = false
                }
            }
        })
    }


    //    썸네일 추가
    private fun addThumbnail() {
        val getThumbnailBtn = findViewById<ConstraintLayout>(R.id.schedule_add_thumbnail_cl)

        getThumbnailBtn.setOnClickListener {
            TedImagePicker.with(this)
                .max(5, "썸네일은 최대 5장까지 추가 가능합니다.")
                .mediaType(MediaType.IMAGE)
                .startMultiImage { uriList ->
                    thumbnailCount = list.size + uriList.size
                    if (thumbnailCount <= 5) {
                        adapter.addThumbnail(uriList)
                        binding.scheduleThumbnailNumTv.text = "$thumbnailCount/5"
                        for(i in 1 .. uriList.size){
                            uriList?.let{
                                    uriList ->
                                context.contentResolver.query(
                                    uriList[i-1],null,null,null,null)
                            }?.use { cursor ->
                                val nameIndex = cursor.getColumnIndex("_data")
                                cursor.moveToFirst()
                                val imgFile = cursor.getString(nameIndex)
                                cursor.close()
                                val test = BitmapFactory.decodeFile(imgFile)
                                val bitmapRequestBody = test?.let { BitmapRequestBody(it) }
                                bitmapMultipartBody.add(MultipartBody.Part.createFormData("thumnails", imgFile, bitmapRequestBody!!))
                            } }
                    } else Toast.makeText(
                        applicationContext,
                        "썸네일은 최대 5장까지 추가 가능합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        //      썸네일 리사이클러뷰 어댑터

        binding.scheduleThumbnailRv.adapter = adapter

        binding.scheduleThumbnailRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }


//    뒤로가기, 일정 정보 재설정, 완료 버튼
    private fun initClickListener() {
        binding.scheduleBackIv.setOnClickListener {
            finish()
        }
    }



    private inner class ScheduleListAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

        override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
            super.onBindViewHolder(holder, position, payloads)
        }

        override fun getItemCount(): Int = days.toInt()

        override fun createFragment(position: Int): Fragment = ScheduleListFragment()

    }


    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): okhttp3.MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 33, sink.outputStream())
        }
    }



    override fun onPostSuccess(code: Int, message: String, result: TravelIdx) {
        LoadingDialog.loadingOff()
        val intent = Intent(this, PostActivity::class.java).apply {
            putExtra("travelIdx",result.travelIdx)
        }
        startActivity(intent)
        finish()
    }

    override fun onPostFailure(code: Int, message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        LoadingDialog.loadingOff()
    }

    override fun onPostLoading() {
        LoadingDialog.loadingOn(this)
    }



    override fun onAddThumbnailSuccess(message: String, result: ArrayList<ThumbnailInfo>) {
        LoadingDialog.loadingOff()
        val resultSize = result.size
        for(i in 1 .. resultSize){
            thumnails.apply {
                add((result[i-1].location))
            }
        }

//        썸네일 등록 성공시 바로 피드 포스트
        val postFeedService = PostFeedService()
        postFeedService.setPostFeedView(this)
        val traffic = binding.scheduleTransportTv.text.toString()
        val title = binding.scheduleFeedTitleEt.text.toString()
        val introduce = binding.scheduleFeedIntroduceEt.text.toString()
        val hashText = binding.scheduleFeedHashtagEt.text.toString()
        val hashing = hashText.trim()
        val hashTagList = hashing.split("#")
        val hashTag = ArrayList<String>(hashTagList)
        hashTag.removeAt(0)
        for(i in 1 .. hashTag.size){
            hashTag[i-1] = hashTag[i-1].trim()
        }
        val startDate = dayIndexViewModel.startDate.value!!
        val endDate = dayIndexViewModel.endDate.value!!
        val jwtToken = getJwt(context)

        information = Information(startDate,endDate,traffic,title,introduce)
        day = dayViewModel.day.value!!
        metadata = MetaData(hashTag,thumnails)
        val postData = PostData(information, day, metadata)
        postFeedService.postFeed(jwtToken,postData)
    }

    override fun onAddThumbnailFailure(code: Int, message: String) {
        Toast.makeText(this,code.toString(),Toast.LENGTH_SHORT).show()
        LoadingDialog.loadingOff()
    }

    override fun onAddThumbnailLoading() {
        LoadingDialog.loadingOn(this)
    }

}











