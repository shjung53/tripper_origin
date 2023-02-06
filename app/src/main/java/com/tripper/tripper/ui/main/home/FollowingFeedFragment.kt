package com.tripper.tripper.ui.main.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.FragmentFollowingFeedBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.LikeService
import com.tripper.tripper.ui.main.feed.LikeView
import com.tripper.tripper.ui.main.feed.TravelIdx
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.feed.userProfile.UserProfileActivity
import com.tripper.tripper.ui.main.home.feedInquiry.FeedData
import com.tripper.tripper.ui.main.home.feedInquiry.FeedInquiryService
import com.tripper.tripper.ui.main.home.feedInquiry.FeedInquiryView
import gun0912.tedimagepicker.util.ToastUtil

class FollowingFeedFragment: Fragment() {

    lateinit var binding: FragmentFollowingFeedBinding

    private var feedData = ArrayList<FeedData>()

    private lateinit var feedInquiryService: FeedInquiryService

    private lateinit var followingFeedRVAdapter : HomeFeedRVAdapter

    private val followFeedViewModel: HomeFeedViewModel by viewModels()

    private lateinit var likeService: LikeService
    private val likeViewModel: LikeViewModel by viewModels()
    var position = 0

    var page = 1
    var savePage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingFeedBinding.inflate(inflater, container, false)

//        followingFeedRVAdapter = HomeFeedRVAdapter(requireContext(), feedData)

//        binding.followingFeedRv.adapter = followingFeedRVAdapter
//
//        binding.followingFeedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        binding.followingFeedRv.addItemDecoration(RVItemDecoration())

//        followFeedViewModel.followPage(page)
//
//        feedInquiryService = FeedInquiryService()
//        feedInquiryService.setFeedInquiryView(this@FollowingFeedFragment)
//
//        likeService = LikeService()
//        likeService.setLikeView(this)

        val token = getJwt(requireContext())
        val option = "팔로우"


//        binding.followingFeedRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                page = followFeedViewModel.followPage.value!!
//                feedData = followFeedViewModel.followFeedData.value!!
//                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                val lastItemPosition = feedData.size-1
//
//                if(lastVisibleItemPosition == lastItemPosition){
//                    if (savePage == page) {
//                        Toast.makeText(ToastUtil.context, "더 이상 피드가 없습니다.", Toast.LENGTH_SHORT).show()
//                    } else {
//                        feedInquiryService.getFeedList(token, option, page)
//                        savePage = page
//                    }
//
//                }
//
//            }
//        })
//
//
//
//        followingFeedRVAdapter.setMyClickListener(object: HomeFeedRVAdapter.MyItemClickListener{
//            override fun onProfileClick(holder: HomeFeedRVAdapter.ViewHolder) {
//                val searchUserIdx = feedData[holder.adapterPosition].userIdx
//                val intent = Intent(context, UserProfileActivity::class.java)
//                intent.putExtra("searchUserIdx", searchUserIdx)
//                startActivity(intent)
//            }
//
//            override fun onItemClickCV(holder: HomeFeedRVAdapter.ViewHolder) {
//                val intent = Intent(context, PostActivity::class.java).apply {
//                    putExtra("travelIdx", feedData[holder.adapterPosition].travelIdx)
//                }
//                startActivity(intent)
//            }
//
//            override fun onHeartClick(holder: HomeFeedRVAdapter.ViewHolder) {
//                holder.binding.itemFeedHeartOffIv.setOnClickListener {
//                    val travelIdx = TravelIdx(feedData[holder.adapterPosition].travelIdx)
//                    position = holder.adapterPosition
//                    likeViewModel.getPosition(position)
//                    if (token != "") {
//                        likeService.like(token, travelIdx)
//                    } else {
//                        Toast.makeText(activity, "로그인 후 이용가능한 서비스 입니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                holder.binding.itemFeedHeartOnIv.setOnClickListener {
//                    val travelIdx = TravelIdx(feedData[holder.adapterPosition].travelIdx)
//                    position = holder.adapterPosition
//                    likeViewModel.getPosition(position)
//                    if (token != "") {
//                        likeService.like(token, travelIdx)
//                    } else {
//                        Toast.makeText(activity, "로그인 후 이용가능한 서비스 입니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//        })



        return binding.root
    }

//    override fun onFeedInquirySuccess(message: String, result: ArrayList<FeedData>) {
//        val size = result.size
//        for(i in 1 .. size){
//            feedData.apply {
//                add(result[i-1])
//            }
//        }
//        followingFeedRVAdapter.getFeed(feedData)
//        followFeedViewModel.followFeedData(feedData)
//        LoadingDialog.loadingOff()
//        followFeedViewModel.followPage(page+1)
//        Log.d("로딩끝3","로딩끝")
//        Toast.makeText(requireContext(),"성공",Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onFeedInquiryFailure(code: Int, message: String) {
//        LoadingDialog.loadingOff()
//    }
//
//    override fun onFeedInquiryLoading() {
//        LoadingDialog.loadingOn(requireContext())
//    }
//
//
//    override fun onLikeSuccess(code: Int, message: String) {
//        LoadingDialog.loadingOff()
//        if (feedData[position].myLikeStatus == "좋아요 하는중") {
//            feedData[position].myLikeStatus = "좋아요 안하는중"
//        } else {
//            feedData[position].myLikeStatus = "좋아요 하는중"
//        }
//        followingFeedRVAdapter.setLike(feedData, position)
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//
//        binding.followingFeedRv.itemAnimator = null
//    }
//
//    override fun onLikeFailure(code: Int, message: String) {
//        LoadingDialog.loadingOff()
//        when (code) {
//            4000, 4001 -> Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
//            else -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onLikeLoading() {
//        LoadingDialog.loadingOn(requireContext())
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if(feedData.size == 0){
//            val token = getJwt(ToastUtil.context)
//            val option = "팔로우"
//            feedInquiryService.getFeedList(token, option, page)
//        }
//    }
}