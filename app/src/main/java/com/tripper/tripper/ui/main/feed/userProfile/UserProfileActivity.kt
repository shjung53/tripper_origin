package com.tripper.tripper.ui.main.feed.userProfile
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.ActivityUserProfileBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.LikeService
import com.tripper.tripper.ui.main.feed.LikeView
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.feed.TravelIdx
import com.tripper.tripper.ui.main.home.LikeViewModel
import com.tripper.tripper.ui.main.home.UserProfileViewModel
import com.tripper.tripper.ui.main.myInfo.follow.FollowService
import com.tripper.tripper.ui.main.myInfo.follow.FollowView
import com.tripper.tripper.ui.main.myInfo.follow.ToIdx
import com.tripper.tripper.ui.main.myInfo.followList.*
import gun0912.tedimagepicker.util.ToastUtil.context

class UserProfileActivity : AppCompatActivity(), FollowView, UserProfileView, LikeView, FollowListView {

    private lateinit var viewBinding: ActivityUserProfileBinding
    private lateinit var followService: FollowService
    private lateinit var userProfileRVAdapter: UserProfileRVAdapter
    private lateinit var userProfileService: UserProfileService
    private var userProfileFeedResult = ArrayList<OtherProfileFeed>()
    private var followListResult = ArrayList<FollowListResult>()
    private lateinit var followListService: FollowListService
    var page = 1
    var savePage = 0
    private lateinit var likeService: LikeService
    private val likeViewModel: LikeViewModel by viewModels()

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val token = getJwt(context)

        val searchUserIdx = intent.getIntExtra("searchUserIdx", 0)
//        getUserIdx(requireContext())


        //팔로잉 팔로우 조회 서비스
        followListService = FollowListService()
        followListService.setFollowListView(this)


        viewBinding.userProfileBackIv.setOnClickListener {
            onBackPressed()
        }

        //팔로잉 팔로우 액티비티 화면 연결

        viewBinding.userProfileFollowerTv.setOnClickListener {

            val option: String = "follower"
            followListService.followList(token, searchUserIdx, option)

        }
        viewBinding.userProfileFollowingTv.setOnClickListener {
            val option: String = "following"
            followListService.followList(token, searchUserIdx, option)
        }

        //상대방 프로필 조회 Api

        userProfileService = UserProfileService()
        userProfileService.setUserProfileView(this)
        userProfileService.userProfile(token, searchUserIdx, page)



        //팔로잉 팔로우 api

        followService = FollowService()
        followService.setFollowView(this)

        likeService = LikeService()
        likeService.setLikeView(this)



        val followingBtn = viewBinding.userProfileFollowingBtn
        val followBtn = viewBinding.userProfileFollowBtn


        followingBtn.setOnClickListener {

            val toIdx = ToIdx(searchUserIdx)

            followService.follow(token, toIdx)



            followingBtn.visibility = View.INVISIBLE
            followBtn.visibility = View.VISIBLE

        }


        followBtn.setOnClickListener {
            val toIdx = ToIdx(searchUserIdx)



            followService.follow(token, toIdx)


            followingBtn.visibility = View.VISIBLE
            followBtn.visibility = View.INVISIBLE
        }

        if (userProfileFeedResult != null) {
            userProfileRVAdapter = UserProfileRVAdapter(userProfileFeedResult)
            userProfileRVAdapter.setMyClickListener(object :
                UserProfileRVAdapter.MyItemClickListener {
                override fun onHeartClick(holder: UserProfileRVAdapter.ViewHolder) {
                    holder.binding.itemUserProfileHeartIv.setOnClickListener {
                        val travelIdx =
                            TravelIdx(userProfileFeedResult[holder.adapterPosition].travelIdx)
                        position = holder.adapterPosition
                        likeViewModel.getPosition(position)
                        likeService.like(token, travelIdx)

                    }
                }

                override fun gotoFeed(holder: UserProfileRVAdapter.ViewHolder) {
                    val intent = Intent(this@UserProfileActivity, PostActivity::class.java).apply {
                        putExtra("travelIdx", userProfileFeedResult[holder.adapterPosition].travelIdx)
                    }
                    startActivity(intent)
                }
            })
        }

        viewBinding.userProfileRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.userProfileRv.adapter = userProfileRVAdapter

        viewBinding.userProfileRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                userProfileFeedResult = userProfileViewModel.userProfileData.value!!
                page = userProfileViewModel.page.value!!
                val lastItemPosition = userProfileFeedResult.size - 1
                if (lastVisibleItemPosition == lastItemPosition && token != null) {
                    // 이전 조회한 page 값과 지금 조회할 page 값이 같으면 서버 실행 막기
                    if (savePage == page) {
                        Toast.makeText(context, "더 이상 피드가 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        userProfileService.userProfile(token, searchUserIdx, page)
                        savePage = page
                    }
                }
            }
        })


    }

    override fun onFollowSuccess(message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onFollowFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            3004, 4000, 4001 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
    }

    override fun onFollowLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onUserProfileSuccess(code: Int, message: String, result: UserProfileResult) {
        LoadingDialog.loadingOff()

        Glide.with(viewBinding.userProfileProfileImageIv)
            .load(result.otherProfileInfo.profileImgUrl)
            .apply(RequestOptions.centerCropTransform()).into(viewBinding.userProfileProfileImageIv)
        viewBinding.userProfileNicknameTv.text = result.otherProfileInfo.nickName
        viewBinding.userProfileFollowingTv.text = "팔로잉   " + result.otherProfileInfo.followingCount
        viewBinding.userProfileFollowerTv.text = "팔로워   " + result.otherProfileInfo.followerCount
        if (result.otherProfileInfo.followStatus == "Y") {
            viewBinding.userProfileFollowingBtn.visibility = View.INVISIBLE
            viewBinding.userProfileFollowBtn.visibility = View.VISIBLE
        } else {
            viewBinding.userProfileFollowingBtn.visibility = View.VISIBLE
            viewBinding.userProfileFollowBtn.visibility = View.INVISIBLE
        }

        when (code) {
            1029 -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (result.otherProfileFeed != null) {
                    userProfileFeedResult.apply {
                        for (i in 1..result.otherProfileFeed.size) {
                            add(result.otherProfileFeed[i - 1])
                        }
                    }
                    userProfileRVAdapter.userProfile(userProfileFeedResult)
                }
                userProfileViewModel.getData(userProfileFeedResult)
                userProfileViewModel.getPage(page + 1)
            }
            3023 -> {
                Toast.makeText(this, "피드가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUserProfileFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000, 4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    override fun onUserProfileLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onLikeSuccess(code: Int, message: String) {
        LoadingDialog.loadingOff()
        if (userProfileFeedResult[position].likeStatus == "좋아요 하는중") {
            userProfileFeedResult[position].likeStatus = "좋아요 안하는중"
        } else {
            userProfileFeedResult[position].likeStatus = "좋아요 하는중"
        }
        userProfileRVAdapter.setLike(userProfileFeedResult, position)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        viewBinding.userProfileRv.itemAnimator = null
    }

    override fun onLikeFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000, 4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLikeLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onFollowListSuccess(
        message: String,
        result: ArrayList<FollowListResult>,
        code: Int
    ) {
        LoadingDialog.loadingOff()
        followListResult = result
        when (code) {
            1009 -> {
                val intent = Intent(this, FollowingActivity::class.java).apply {
                    putParcelableArrayListExtra("result", followListResult)
                    putExtra("who", "other")

                }
                startActivity(intent)

            }
            1010 -> {
                val intent = Intent(this, FollowerActivity::class.java).apply {
                    putParcelableArrayListExtra("result", followListResult)

                }
                startActivity(intent)
            }
        }
    }

    override fun onFollowListFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {

            3004, 3005, 3006 -> Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show()
            4000, 4001 -> Toast.makeText(this, "서버오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFollowListLoading() {
        LoadingDialog.loadingOn(this)
    }
}