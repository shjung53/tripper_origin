package com.tripper.tripper.ui.main.feed.post
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.*
import com.tripper.tripper.databinding.ActivityPostBinding
import com.rd.PageIndicatorView
import com.tripper.tripper.dataClass.AreaLocation
import com.tripper.tripper.dataClass.DatesData
import com.tripper.tripper.dataClass.PostDayData
import com.tripper.tripper.dataClass.ScheduleData
import com.tripper.tripper.ui.main.feed.*
import com.tripper.tripper.ui.main.feed.dayInquiry.DayData
import com.tripper.tripper.ui.main.feed.dayInquiry.DayInquiryService
import com.tripper.tripper.ui.main.feed.dayInquiry.DayInquiryView
import com.tripper.tripper.ui.main.feed.searchPost.*
import com.tripper.tripper.ui.main.feed.userProfile.UserProfileActivity
import gun0912.tedimagepicker.util.ToastUtil
import gun0912.tedimagepicker.util.ToastUtil.context
import net.daum.mf.map.api.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class PostActivity : AppCompatActivity() , ScoreView, SearchPostView{

    private lateinit var viewBinding: ActivityPostBinding
    private lateinit var scoreService: ScoreService
    private lateinit var searchPostService: SearchPostService
    private lateinit var postVPAdapter : PostVPAdapter
    private lateinit var travelInfo: TravelInfo
    private var thumbnails = ArrayList<String?>()
    private var dayIdx = ArrayList<Int>()
    private lateinit var postListVPAdapter : PostListVPAdapter
    private lateinit var postDateRVAdapter : PostDateRVAdapter
    private val postViewModel : PostViewModels by viewModels()
    private var dayData = ArrayList<PostDayData>()
    private var areaLocation = ArrayList<AreaLocation>()
    private var scheduleData = ArrayList<ScheduleData>()

    private var dates = ArrayList<String>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN)
    private val yearRange = IntRange(0,3)
    private val monthRange = IntRange(5,6)
    private val dayRange = IntRange(8,9)

    private var dateIndex = 0

    var dialogView:View? = null
    var alertDialog: AlertDialog? = null
    var dialogBtn: AppCompatButton? = null
    var dialogCloseBtn: ImageView? = null
    var veryBadBtn: LinearLayout? = null
    var veryBadTv: TextView? = null
    var veryBadIv: ImageView? = null
    var badBtn: LinearLayout? = null
    var badTv: TextView? = null
    var badIv: ImageView? = null
    var normalBtn: LinearLayout? = null
    var normalTv: TextView? = null
    var normalIv: ImageView? = null
    var goodBtn: LinearLayout? = null
    var goodTv: TextView? = null
    var goodIv: ImageView? = null
    var veryGoodBtn: LinearLayout? = null
    var veryGoodTv: TextView? = null
    var veryGoodIv: ImageView? = null
    var goneLayout: ConstraintLayout? = null
    var selectedGrade: LinearLayout? = null
    var selectedGradeTv: TextView? = null
    var selectedGradeIv: ImageView? = null


    val token = getJwt(context)


    override fun onCreate(savedInstanceState: Bundle?) {
        val travelIdx = intent.getIntExtra("travelIdx",0)
        postViewModel.getFeedIdx(travelIdx)
        super.onCreate(savedInstanceState)

        postViewModel.getDateIdx(dateIndex)

        viewBinding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        searchPostService = SearchPostService()
        searchPostService.getSearchPostView(this)
        searchPostService.searchPost(token, travelIdx)



        val alertDialogBuilder = AlertDialog.Builder(this, R.style.NewDialog)

        viewBinding.postBottomRateLinear.setOnClickListener {
            if (dialogView?.parent != null) {
                (dialogView?.parent as ViewGroup).removeView(dialogView)
            }
            setDialogView()
            dialogBtn?.isClickable = false
            alertDialog = alertDialogBuilder.setView(dialogView).create()
            alertDialog?.show()
        }

        initClickListener()




//        맵뷰

        val mapView = MapView(this)
        viewBinding.postKakaoMapCv.addView(mapView)



//        맵뷰 세팅



            postViewModel.scheduleData.observe(this) {
                mapView.removeAllPolylines()
                mapView.removeAllPOIItems()
                areaLocation.clear()
                dayData.clear()
                val polyline = MapPolyline()
                polyline.lineColor = R.color.main
                dateIndex = postViewModel.dateIdx.value!!
                scheduleData = postViewModel.scheduleData.value!!
                dayData = scheduleData[dateIndex].dayData

                    for (i in 1..dayData.size) {
                        areaLocation.add(i - 1, AreaLocation(dayData[i - 1].areaLatitude, dayData[i - 1].areaLongitude))
                    }
                    for (i in 1..dayData.size) {
                        mapView.addPOIItem(makeMarker(dayData[i - 1].areaName,
                            MapPoint.mapPointWithGeoCoord(dayData[i - 1].areaLatitude, dayData[i - 1].areaLongitude),
                            dayData[i - 1].areaCategory))
                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dayData[i - 1].areaLatitude, dayData[i - 1].areaLongitude)
                        )
                        mapView.addPolyline(polyline)
                        // polyLine 모두 나오도록
                        val mapPointBounds = MapPointBounds(polyline.mapPoints)
                        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100))
                    }
        }

        postViewModel.dateIdx.observe(this){
            if(postViewModel.scheduleData.value != null && postViewModel.dayIdx.value?.size == postViewModel.scheduleData.value?.size){
                mapView.removeAllPOIItems()
                mapView.removeAllPolylines()
            }
        }








        // (수정, 삭제) + (신고, 취소) 다이얼로그 관련
        val editDialogView = layoutInflater.inflate(R.layout.dialog_edit, null)
        val reportDialogView = layoutInflater.inflate(R.layout.dialog_report, null)

        val alertEditDialog = AlertDialog.Builder(this, R.style.NewDialog)
            .setView(editDialogView)
            .create()

        val alertReportDialog = AlertDialog.Builder(this, R.style.NewDialog)
            .setView(reportDialogView)
            .create()

        val dialogEditBtn = editDialogView.findViewById<CardView>(R.id.dialog_edit_edit_cv) // (수정, 삭제)에서 수정 버튼
        val dialogDeleteBtn = editDialogView.findViewById<CardView>(R.id.dialog_edit_delete_cv) // (수정, 삭제)에서 삭제 버튼

        val dialogReportBtn = reportDialogView.findViewById<CardView>(R.id.dialog_report_report_cv) // (신고, 취소)에서 신고 버튼
        val dialogCancelBtn = reportDialogView.findViewById<CardView>(R.id.dialog_report_cancel_cv) // (신고, 취소)에서 취소 버튼

        // (수정, 삭제) 다이얼로그 띄우기 => alertEditDialog.show()
        // (수정, 삭제) 다이얼로그 닫기 => alertEditDialog.dismiss()

        viewBinding.postThreeDotsIv.setOnClickListener {
            alertReportDialog.show() // (신고, 취소) 다이얼로그 띄우기
        }

        dialogCancelBtn.setOnClickListener {
            alertReportDialog.dismiss() // (신고, 취소) 다이얼로그 닫기
        }

        //썸네일 뷰페이저 어댑터 연결
        postVPAdapter = PostVPAdapter(this)

        viewBinding.postVp.adapter = postVPAdapter
        viewBinding.postVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        //일정선택 어탭터 연결
        postListVPAdapter = PostListVPAdapter(this)

        val postListVP = viewBinding.postListVp

        postListVP.adapter = postListVPAdapter
        postListVP.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        postDateRVAdapter = PostDateRVAdapter(this,dateIndex,dayIdx,dates)

        viewBinding.postDateRv.adapter = postDateRVAdapter

        viewBinding.postDateRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        postDateRVAdapter.setMyItemClickListener(object : PostDateRVAdapter.MyItemClickListener {
            override fun movePage(holder: PostDateRVAdapter.ViewHolder, position: Int) {
                dateIndex = holder.adapterPosition
                postListVP.setCurrentItem(dateIndex, true)
                setDate(holder)
                if(dateIndex != postViewModel.dateIdx.value!!){
                postViewModel.getDateIdx(dateIndex)}
            }

            override fun selectMove(holder: PostDateRVAdapter.ViewHolder) {
                postListVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        dateIndex = postListVP.currentItem
                        setDate(holder)
                        if(dateIndex != postViewModel.dateIdx.value!!){
                            postViewModel.getDateIdx(dateIndex)}
                    }
                })
            }
        })






    }

    private fun setDate(holder: PostDateRVAdapter.ViewHolder) {
        if (dateIndex == holder.adapterPosition) {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector)
            holder.binding.itemScheduleDateTv1.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.binding.itemScheduleDateTv2.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        } else {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector2)
            holder.binding.itemScheduleDateTv1.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightBlack
                )
            )
            holder.binding.itemScheduleDateTv2.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightBlack
                )
            )
        }
    }


    private fun initClickListener() {
        viewBinding.postBackIv.setOnClickListener {
            onBackPressed()
        }

        viewBinding.postBottomCommentConstraint.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("feedIdx", travelInfo.travelIdx)
            startActivity(intent)
        }

        //좋아요한 사람 목록 클릭 기능 제거
//        viewBinding.postBottomHeartConstraint.setOnClickListener {
//            val intent = Intent(this, LikeListActivity::class.java)
//            startActivity(intent)
//        }

        viewBinding.postProfileIv.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        viewBinding.postListScrollUpIv.setOnClickListener {
            viewBinding.postScroll.smoothScrollTo(0,0)
        }
    }


    //    평점 다이얼로그
    private fun setDialogView() {
        dialogView = layoutInflater.inflate(R.layout.dialog_rate, null)
        dialogBtn = dialogView?.findViewById<AppCompatButton>(R.id.dialog_grade_button)
        dialogCloseBtn = dialogView?.findViewById<ImageView>(R.id.dialog_grade_x_iv)
        veryBadBtn = dialogView?.findViewById<LinearLayout>(R.id.dialog_grade_verybad_ll)
        veryBadTv = dialogView?.findViewById<TextView>(R.id.dialog_grade_verybad_tv)
        veryBadIv = dialogView?.findViewById<ImageView>(R.id.dialog_grade_verybad_iv)
        badBtn = dialogView?.findViewById<LinearLayout>(R.id.dialog_grade_bad_ll)
        badTv = dialogView?.findViewById<TextView>(R.id.dialog_grade_bad_tv)
        badIv = dialogView?.findViewById<ImageView>(R.id.dialog_grade_bad_iv)
        normalBtn = dialogView?.findViewById<LinearLayout>(R.id.dialog_grade_normal_ll)
        normalTv = dialogView?.findViewById<TextView>(R.id.dialog_grade_normal_tv)
        normalIv = dialogView?.findViewById<ImageView>(R.id.dialog_grade_normal_iv)
        goodBtn = dialogView?.findViewById<LinearLayout>(R.id.dialog_grade_good_ll)
        goodTv = dialogView?.findViewById<TextView>(R.id.dialog_grade_good_tv)
        goodIv = dialogView?.findViewById<ImageView>(R.id.dialog_grade_good_iv)
        veryGoodBtn = dialogView?.findViewById<LinearLayout>(R.id.dialog_grade_verygood_ll)
        veryGoodTv = dialogView?.findViewById<TextView>(R.id.dialog_grade_verygood_tv)
        veryGoodIv = dialogView?.findViewById<ImageView>(R.id.dialog_grade_verygood_iv)
        goneLayout = dialogView?.findViewById<ConstraintLayout>(R.id.dialog_grade_cl)
        selectedGrade = dialogView?.findViewById<LinearLayout>(R.id.dialog_select_grade_ll)
        selectedGradeTv = dialogView?.findViewById<TextView>(R.id.dialog_select_grade_tv)
        selectedGradeIv = dialogView?.findViewById<ImageView>(R.id.dialog_select_grade_iv)
        var score = 0

        dialogCloseBtn?.setOnClickListener {
            alertDialog?.dismiss()
        }

        veryBadBtn?.setOnClickListener {
            dialogBtn?.isClickable = true
            dialogBtn?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryBadTv?.setTextColor(ContextCompat.getColor(this, R.color.main))
            selectedGradeIv?.setImageResource(R.drawable.ic_verybad_29dp)
            selectedGradeTv?.text = "별로에요"
            veryBadIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            badIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            badTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            normalIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            normalTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            goodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            goodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            veryGoodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryGoodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            score = 1
        }

        badBtn?.setOnClickListener {
            dialogBtn?.isClickable = true
            dialogBtn?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            badTv?.setTextColor(ContextCompat.getColor(this, R.color.main))
            selectedGradeIv?.setImageResource(R.drawable.ic_bad_29dp)
            selectedGradeTv?.text = "도움되지 않았어요"
            badIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryBadIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryBadTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            normalIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            normalTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            goodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            goodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            veryGoodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryGoodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            score = 2
        }

        normalBtn?.setOnClickListener {
            dialogBtn?.isClickable = true
            dialogBtn?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            normalTv?.setTextColor(ContextCompat.getColor(this, R.color.main))
            selectedGradeIv?.setImageResource(R.drawable.ic_nomal_29dp)
            selectedGradeTv?.text = "그저 그래요"
            normalIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryBadIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryBadTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            badIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            badTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            goodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            goodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            veryGoodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryGoodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            score = 3
        }

        goodBtn?.setOnClickListener {
            dialogBtn?.isClickable = true
            dialogBtn?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            goodTv?.setTextColor(ContextCompat.getColor(this, R.color.main))
            selectedGradeIv?.setImageResource(R.drawable.ic_good_29dp)
            selectedGradeTv?.text = "도움되었어요!"
            goodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryBadIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryBadTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            badIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            badTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            normalIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            normalTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            veryGoodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryGoodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            score = 4
        }

        veryGoodBtn?.setOnClickListener {
            dialogBtn?.isClickable = true
            dialogBtn?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryGoodTv?.setTextColor(ContextCompat.getColor(this, R.color.main))
            selectedGradeIv?.setImageResource(R.drawable.ic_verygood_29dp)
            selectedGradeTv?.text = "최고의 여행!"
            veryGoodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.main)
            veryBadIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            veryBadTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            badIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            badTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            normalIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            normalTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            goodIv?.backgroundTintList = ContextCompat.getColorStateList(this, R.color.darkGray)
            goodTv?.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
            score = 5
        }

        dialogBtn?.setOnClickListener {
            scoreService = ScoreService()
            scoreService.setScoreView(this)
            val travelIdx = travelInfo.travelIdx
            val scoreData = ScoreData(travelIdx, score)
            val token = getJwt(context)
            scoreService.score(token,scoreData)
        }


    }

    private fun makeMarker(itemName: String, mapPoint: MapPoint, image: String) : MapPOIItem {
        val marker = MapPOIItem()
        marker.itemName = itemName
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        // "관광명소", "음식점" + "카페", "숙박"
        if (image == "음식점" || image == "카페") {
            marker.customImageResourceId = R.drawable.ic_food_marker_5x
        }
        else if (image == "관광명소") {
            marker.customImageResourceId = R.drawable.ic_landscape_marker_5x
        }
        else if (image == "숙박") {
            marker.customImageResourceId = R.drawable.ic_bed_marker_5x
        }
        else {
            marker.customImageResourceId = R.drawable.ic_etc_marker_5x
        }
        marker.setCustomImageAnchor(0.5f, 0.73f)


        return marker
    }

    override fun onScoreSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        goneLayout?.visibility = View.GONE
        dialogBtn?.visibility = View.GONE
        selectedGrade?.visibility = View.VISIBLE
        val travelIdx = intent.getIntExtra("travelIdx",0)
        searchPostService.searchPost(token, travelIdx)

    }

    override fun onScoreFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when(code) {
            4000,4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        alertDialog?.dismiss()
    }

    override fun onScoreLoading() {
        LoadingDialog.loadingOn(this)
    }

//    일정 게시물 로드 성공
    override fun onSearchPostSuccess(message: String, result: SearchPostResult) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        viewBinding.postIdTv.text = result.travelInfo.nickName
        viewBinding.postTitleTv.text = result.travelInfo.title
        viewBinding.postHashTagTv.text = result.travelInfo.travelHashtag
        viewBinding.postPostTv.text = result.travelInfo.introduce
        Glide.with(viewBinding.postProfileIv).load(result.travelInfo.profileImgUrl).apply(
            RequestOptions.centerCropTransform()).into(viewBinding.postProfileIv)
        viewBinding.postRateTv.text = result.travelInfo.travelScore
        viewBinding.postBottomHeartCountTv.text = result.travelInfo.totalLikeCount.toString()
        viewBinding.postBottomCommentCountTv.text = result.travelInfo.totalCommentCount.toString()
        viewBinding.postBottomRateTv.text = result.travelInfo.myScore
        val postRateImg = viewBinding.postRateIv
        val postRateTv = viewBinding.postRateTv
        when(result.travelInfo.travelScore) {
            "최고의 여행!" -> {
                postRateImg.setImageResource(R.drawable.ic_verygood_29dp)
                postRateTv.text = "최고의 여행!"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "도움되었어요!" -> {
                postRateImg.setImageResource(R.drawable.ic_good_29dp)
                postRateTv.text = "도움되었어요!"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "그저 그래요" -> {
                postRateImg.setImageResource(R.drawable.ic_nomal_29dp)
                postRateTv.text = "그저 그래요"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "도움되지 않았어요" -> {
                postRateImg.setImageResource(R.drawable.ic_bad_29dp)
                postRateTv.text = "도움되지 않았어요"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "별로에요" -> {
                postRateImg.setImageResource(R.drawable.ic_verybad_29dp)
                postRateTv.text = "별로에요"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "점수 없음" -> {
                postRateImg.setImageResource(R.drawable.ic_gray_smile)
                postRateTv.text = "아직 평점이 없어요"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.darkGray))
            }
            else -> {
                postRateImg.setImageResource(R.drawable.ic_gray_smile)
                postRateTv.text = "아직 평점이 없어요"
                postRateTv.setTextColor(ContextCompat.getColor(context, R.color.darkGray))
            }
        }
        val bottomPostRateImg = viewBinding.postBottomRateIv
        val bottomPostRateTv = viewBinding.postBottomRateTv
        when(result.travelInfo.myScore) {
            "최고의 여행!" -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_verygood_29dp)
                bottomPostRateTv.text = "최고의 여행!"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "도움되었어요!" -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_good_29dp)
                bottomPostRateTv.text = "도움되었어요!"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "그저 그래요" -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_nomal_29dp)
                bottomPostRateTv.text = "그저 그래요"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "도움되지 않았어요" -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_bad_29dp)
                bottomPostRateTv.text = "도움되지 않았어요"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            "별로에요" -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_verybad_29dp)
                bottomPostRateTv.text = "별로에요"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.main))
            }
            null -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_gray_smile)
                bottomPostRateTv.text = "나의 여행"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.darkGray))
            }
            else -> {
                bottomPostRateImg.setImageResource(R.drawable.ic_gray_smile)
                bottomPostRateTv.text = "어땠나요?"
                bottomPostRateTv.setTextColor(ContextCompat.getColor(context, R.color.darkGray))
            }
        }
        if (result.travelInfo.myLikeStatus == "좋아요 하는중"){
            viewBinding.postBottomHeartIv.setImageResource(R.drawable.ic_heart_full)
        }else{
            viewBinding.postBottomHeartIv.setImageResource(R.drawable.ic_emptyheart)
        }
        dayIdx = result.travelDayIdx
        postDateRVAdapter.setDate(dayIdx)

        postViewModel.getDayIdx(dayIdx)


        for(i in 1 .. dayIdx.size){
            postListVPAdapter.addFragment(PostListVPFragment())
        }



        if(thumbnails.size < 1) {
            thumbnails = result.travelThumnails
            for (i in 1..thumbnails.size) {
                postVPAdapter.addFragment(PostVPFragment(thumbnails[i - 1]!!))
            }
        }

        travelInfo = result.travelInfo

        val vpIndicatorView: PageIndicatorView = viewBinding.postVpIndicator
        vpIndicatorView.count = postVPAdapter.itemCount


        viewBinding.postVp.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vpIndicatorView.selection = position
            }
        })

//    데이 날짜 동기화
    val startDate = result.travelInfo.startDate
    val year = startDate.slice(yearRange).toInt()
    val month = startDate.slice(monthRange).toInt()-1
    val day = startDate.slice(dayRange).toInt()
    val calendar = java.util.Calendar.getInstance()
    calendar.set(year,month,day)

    for(i in 1 .. result.travelDayIdx.size){
        dates.add(dateFormat.format(calendar.time))
        calendar.add(Calendar.DATE, 1)
    }
    postDateRVAdapter.setDateText(dates)
    }

    //    일정 게시물 로드 실패
    override fun onSearchPostFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000, 4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSearchPostLoading() {
        LoadingDialog.loadingOn(this)
    }


}






