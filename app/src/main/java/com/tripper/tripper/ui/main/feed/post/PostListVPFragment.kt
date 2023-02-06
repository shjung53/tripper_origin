package com.tripper.tripper.ui.main.feed.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.dataClass.*
import com.tripper.tripper.databinding.FragmentPostListVpBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.dayInquiry.DayData
import com.tripper.tripper.ui.main.feed.dayInquiry.DayInquiryService
import com.tripper.tripper.ui.main.feed.dayInquiry.DayInquiryView
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewData
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewInquiryService
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewInquiryView

class PostListVPFragment() : Fragment(), DayInquiryView, ReviewInquiryView {

    private lateinit var viewBinding: FragmentPostListVpBinding
    private lateinit var dayInquiryService : DayInquiryService
    private var dayData = ArrayList<PostDayData>()
    private var scheduleData = ArrayList<ScheduleData>()
    private val postViewModel : PostViewModels by activityViewModels()
    private val reviewViewModel : ReviewViewModels by activityViewModels()
    private var feedIdx = 0
    private var dateIdx = 0
    private var areaIdx = 0
    private var reviewPos = 0
    private var dayIdxList = ArrayList<Int>()
    private var dayIdx = 0
    private lateinit var postListRVAdapter : PostListRVAdapter
    private lateinit var reviewInquiryService: ReviewInquiryService
    private var reviewData : ReviewData? = null
    private var allReviews = ArrayList<ReviewData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentPostListVpBinding.inflate(inflater, container, false)

        dayInquiryService = DayInquiryService()
        dayInquiryService.setDayInquiryView(this)
        reviewInquiryService = ReviewInquiryService()
        reviewInquiryService.setReviewInquiryView(this)


        postListRVAdapter = PostListRVAdapter(requireContext(), dayData, allReviews)

        viewBinding.postListRv.adapter = postListRVAdapter
        val layoutManagerWrapper = LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.postListRv.layoutManager = layoutManagerWrapper

        postListRVAdapter.setMyClickListener(object: PostListRVAdapter.MyItemClickListener {
            override fun itemOpen(holder: PostListRVAdapter.ViewHolder){
                val token = getJwt(requireContext())
                feedIdx = postViewModel.feedIdx.value!!
                dayIdxList = postViewModel.dayIdx.value!!
                dateIdx = postViewModel.dateIdx.value!!
                areaIdx = postViewModel.dayData.value!![holder.adapterPosition].areaIdx
                reviewPos = holder.adapterPosition
                dayIdx = dayIdxList[dateIdx]
                reviewViewModel.getReviewPos(reviewPos)
                reviewInquiryService.getReview(token,feedIdx,dayIdx,areaIdx)
            }

            override fun goReview(holder: PostListRVAdapter.ViewHolder) {
                val intent = Intent(requireContext(), ReviewContentActivity::class.java).apply {
                    allReviews = reviewViewModel.allReview.value!!
                    dayData = postViewModel.dayData.value!!
                    putExtra("reviewComment",allReviews[holder.adapterPosition].travelAreaReviewComment)
                    putStringArrayListExtra("reviewImg",allReviews[holder.adapterPosition].travelAreaReviewImage)
                    putExtra("placeName", dayData[holder.adapterPosition].areaName )
                }
                startActivity(intent)
            }
        })


        return viewBinding.root
    }

    override fun onDaySuccess(code: Int, message: String, result: ArrayList<DayData>) {
        if(postViewModel.scheduleData.value != null){
        scheduleData = postViewModel.scheduleData.value!!}
        dateIdx = postViewModel.dateIdx.value!!
        if(result.size > 0) {
            for (i in 1..result.size){
                dayData.apply {
                    add(
                        PostDayData(
                            when (result[i - 1].areaCategory) {
                                "음식점" -> 1
                                "카페" -> 1
                                "관광명소" -> 2
                                "숙박" -> 3
                                else -> 4 },
                            result[i - 1].areaIdx!!, result[i - 1].dayIdx, result[i - 1].areaCategory!!, result[i - 1].areaName!!,
                            result[i - 1].areaLatitude!!, result[i - 1].areaLongitude!!
                        )
                    )
                }
            allReviews.apply {
                add(ReviewData(ArrayList<String>(),null))
                allReviews[0].travelAreaReviewImage!!.add(" ")
            }
            }
            reviewViewModel.getAllReview(allReviews)
        }
        postViewModel.getDayData(dayData)
        postListRVAdapter.setData(dayData)
        scheduleData.add(dateIdx, ScheduleData(dayData))
        postViewModel.getScheduleData(scheduleData)
        LoadingDialog.loadingOff()
    }

    override fun onDayFailure(code: Int, message: String) {
        Toast.makeText(requireContext(), "다시 시도해 주세요", Toast.LENGTH_SHORT).show()
        LoadingDialog.loadingOff()
    }

    override fun onDayLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onResume() {
        super.onResume()
        val token = getJwt(requireContext())
        if(dayData.size == 0){
            feedIdx = postViewModel.feedIdx.value!!
            dayIdxList = postViewModel.dayIdx.value!!
            dateIdx = postViewModel.dateIdx.value!!
        dayInquiryService.getDayInfo(token,feedIdx,dayIdxList[dateIdx])
        }
        viewBinding.root.requestLayout()
    }

    override fun onReviewSuccess(code: Int, message: String, result: ReviewData) {
        LoadingDialog.loadingOff()
        reviewData = result
        reviewPos = reviewViewModel.reviewPos.value!!
        allReviews = reviewViewModel.allReview.value!!
        allReviews[reviewPos] = reviewData!!
        reviewViewModel.getAllReview(allReviews)
        postListRVAdapter.setReviewData(allReviews,reviewPos)
        reviewViewModel.getSignalReview(1)
    }

    override fun onReviewFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onReviewLoading() {
        LoadingDialog.loadingOn(requireContext())
    }


}


