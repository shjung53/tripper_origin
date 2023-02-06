package com.tripper.tripper.ui.main.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.FragmentHotFeedBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.*
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.feed.userProfile.UserProfileActivity
import com.tripper.tripper.ui.main.home.feedInquiry.FeedData
import com.tripper.tripper.ui.main.home.feedInquiry.FeedInquiryService
import com.tripper.tripper.ui.main.home.feedInquiry.FeedInquiryView
import gun0912.tedimagepicker.util.ToastUtil

class HotFeedFragment: Fragment() , LikeView, FeedInquiryView {
    lateinit var binding: FragmentHotFeedBinding
    private lateinit var likeService: LikeService

    private var feedData = ArrayList<FeedData>()

    private lateinit var feedInquiryService: FeedInquiryService

    private lateinit var hotFeedRVAdapter: HomeFeedRVAdapter

    private val likeViewModel: LikeViewModel by viewModels()
    private val hotFeedViewModel: HomeFeedViewModel by viewModels()

    var page = 1
    var savePage = 0
    var position = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHotFeedBinding.inflate(inflater, container, false)

        likeService = LikeService()
        likeService.setLikeView(this@HotFeedFragment)
        feedInquiryService = FeedInquiryService()
        feedInquiryService.setFeedInquiryView(this)

        val token = getJwt(requireContext())

        hotFeedRVAdapter = HomeFeedRVAdapter(requireContext(),feedData)

        binding.hotFeedRv.adapter = hotFeedRVAdapter

        binding.hotFeedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.hotFeedRv.addItemDecoration(RVItemDecoration())

        hotFeedViewModel.hotPage(page)

        feedInquiryService = FeedInquiryService()
        feedInquiryService.setFeedInquiryView(this@HotFeedFragment)




        binding.hotFeedRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                page = hotFeedViewModel.hotPage.value!!
                feedData = hotFeedViewModel.hotFeedData.value!!
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val lastItemPosition = feedData.size-1

                if(lastVisibleItemPosition == lastItemPosition){
                    if (savePage == page) {
                        Toast.makeText(ToastUtil.context, "더 이상 피드가 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val option = "최신순"
                        feedInquiryService.getFeedList(token, option, page)
                        savePage = page
                    }
                }
            }
        })


        hotFeedRVAdapter.setMyClickListener(object: HomeFeedRVAdapter.MyItemClickListener{
            override fun onProfileClick(holder: HomeFeedRVAdapter.ViewHolder) {
                val searchUserIdx = feedData[holder.adapterPosition].userIdx
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("searchUserIdx", searchUserIdx)
                startActivity(intent)
            }

            override fun onItemClickCV(holder: HomeFeedRVAdapter.ViewHolder) {
                val travelIdx = feedData[holder.adapterPosition].travelIdx
                val intent = Intent(context, PostActivity::class.java).apply {
                    putExtra("travelIdx",travelIdx)
                }
                startActivity(intent)
            }

            override fun onHeartClick(holder: HomeFeedRVAdapter.ViewHolder) {
                holder.binding.itemFeedHeartOffIv.setOnClickListener {
                    val travelIdx = TravelIdx(feedData[holder.adapterPosition].travelIdx)
                    position = holder.adapterPosition
                    likeViewModel.getPosition(position)
                    likeService.like(token, travelIdx)
                }
                holder.binding.itemFeedHeartOnIv.setOnClickListener {
                    val travelIdx = TravelIdx(feedData[holder.adapterPosition].travelIdx)
                    position = holder.adapterPosition
                    likeViewModel.getPosition(position)
                    likeService.like(token, travelIdx)

                }
            }
        })

        return binding.root
    }

    override fun onLikeSuccess(code: Int, message: String) {
        LoadingDialog.loadingOff()
        if (feedData[position].myLikeStatus == "좋아요 하는중") {
            feedData[position].myLikeStatus = "좋아요 안하는중"
        } else {
            feedData[position].myLikeStatus = "좋아요 하는중"
        }
        hotFeedRVAdapter.setLike(feedData, position)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        binding.hotFeedRv.itemAnimator = null
    }

    override fun onLikeFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000,4001 -> Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLikeLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onFeedInquirySuccess(message: String, result: ArrayList<FeedData>) {
        LoadingDialog.loadingOff()
        val size = result.size
        for(i in 1 .. size){
            feedData.apply {
                add(result[i-1])
            }
        }
        hotFeedRVAdapter.getFeed(feedData)
        hotFeedViewModel.hotFeedData(feedData)
        hotFeedViewModel.hotPage(page+1)
    }

    override fun onFeedInquiryFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onFeedInquiryLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onResume() {
        super.onResume()
        if(feedData.size == 0){
            val token = getJwt(requireContext())
            val option = "최신순"
            feedInquiryService.getFeedList(token, option, page)
        }
    }
}