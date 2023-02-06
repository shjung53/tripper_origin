package com.tripper.tripper.ui.main.myInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.FragmentLikeBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.LikeService
import com.tripper.tripper.ui.main.feed.LikeView
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.feed.TravelIdx
import com.tripper.tripper.ui.main.home.LikeViewModel
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.*
import gun0912.tedimagepicker.util.ToastUtil

class LikeFragment: Fragment(), MyInfoView , LikeView {
    lateinit var viewBinding: FragmentLikeBinding
    private lateinit var myInfoService: MyInfoService
    private var userMyPageFeedByOption = ArrayList<UserMyPageFeedByOption>()
    private lateinit var likeRVAdapter: LikeRVAdapter
    private val likedViewModel: MyTripViewModel by viewModels()
    var page = 1
    private lateinit var likeService: LikeService
    private val likeViewModel: LikeViewModel by viewModels()
    var position = 0
    var savePage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLikeBinding.inflate(layoutInflater,container,false)

        val token = getJwt(ToastUtil.context)
        val search: String = "좋아요"

        myInfoService = MyInfoService()
        myInfoService.setMyInfoView(this)
        myInfoService.myPageInfo(token, search, page)

        likeService = LikeService()
        likeService.setLikeView(this)


        likeRVAdapter = LikeRVAdapter(userMyPageFeedByOption)


        viewBinding.likeRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        viewBinding.likeRcv.adapter = likeRVAdapter

        likeRVAdapter.setMyClickListener(object: LikeRVAdapter.MyItemClickListener{
            override fun onHeartClick(holder: LikeRVAdapter.ViewHolder) {
                holder.binding.itemLikeHeartIv.setOnClickListener {
                    val travelIdx = TravelIdx(userMyPageFeedByOption[holder.adapterPosition].travelIdx)
                    position = holder.adapterPosition
                    likeViewModel.getPosition(position)
                    likeService.like(token,travelIdx)
                }
            }

            override fun gotoFeed(holder: LikeRVAdapter.ViewHolder) {
                val intent = Intent(requireContext(), PostActivity::class.java).apply {
                    putExtra("travelIdx",userMyPageFeedByOption[holder.adapterPosition].travelIdx)
                }
                startActivity(intent)
            }
        })

        viewBinding.likeRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                userMyPageFeedByOption = likedViewModel.likedData.value!!
                page = likedViewModel.page.value!!
                val itemTotalPosition = userMyPageFeedByOption.size - 1
                if (lastVisibleItemPosition == itemTotalPosition && token != null) {

                    if(savePage == page){
                        Toast.makeText(ToastUtil.context, "더 이상 피드가 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        myInfoService.myPageInfo(token, search, page)
                        savePage = page
                    }

                }
            }
        })


        return viewBinding.root
    }

    override fun onMyInfoSuccess(message: String, result: UserMyPageResult,code: Int) {
        LoadingDialog.loadingOff()
        if(code == 1028){
        if(result.userMyPageFeedByOption != null){
        userMyPageFeedByOption.apply {
            for (i in 1 .. result.userMyPageFeedByOption!!.size){
                add(result.userMyPageFeedByOption!![i-1])
            }}
        }
        likeRVAdapter.getFeedInfo(userMyPageFeedByOption)

        likedViewModel.getLikedData(userMyPageFeedByOption)
        likedViewModel.getPage(page+1)
        }
    }

    override fun onMyInfoFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onLikeSuccess(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            1019 -> {
                userMyPageFeedByOption.removeAt(position)
                likedViewModel.getLikedData(userMyPageFeedByOption)
                likeRVAdapter.removeLike(userMyPageFeedByOption, position)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                viewBinding.likeRcv.itemAnimator = null
            }
        }
    }

    override fun onLikeFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000,4001 -> Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLikeLoading() {
        LoadingDialog.loadingOn(requireContext())
    }
}