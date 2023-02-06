package com.tripper.tripper.ui.main.myInfo.searchMyInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.FragmentMyInfoBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.myInfo.LikeFragment
import com.tripper.tripper.ui.main.myInfo.MyTripFragment
import com.tripper.tripper.ui.setting.*
import com.google.android.material.tabs.TabLayoutMediator
import com.tripper.tripper.getUserIdx
import com.tripper.tripper.ui.main.myInfo.follow.FollowService
import com.tripper.tripper.ui.main.myInfo.followList.*
import gun0912.tedimagepicker.util.ToastUtil
import kotlinx.coroutines.currentCoroutineContext


class MyInfoFragment: Fragment(), ProfileSettingView, MyInfoView, FollowListView {

    lateinit var binding: FragmentMyInfoBinding
    private lateinit var profileSettingService: ProfileSettingService
    private lateinit var myInfoService: MyInfoService
    private lateinit var followListService: FollowListService
    private var followListResult = ArrayList<FollowListResult>()
    private lateinit var userMyInfo: UserMyPageInfo


    val menu = arrayListOf("My Trip","좋아요")

    lateinit var myTrip: MyTripFragment
    lateinit var like: LikeFragment
    private lateinit var token : String
    private lateinit var search: String
    private val page: Int = 1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyInfoBinding.inflate(inflater, container, false)

        val userIdx = getUserIdx(requireContext())

        token = getJwt(requireContext())


        followListService = FollowListService()
        followListService.setFollowListView(this)


        val myInfoAdapter = MyInfoVPAdapter(this)
        binding.myInfoVp.adapter = myInfoAdapter


        TabLayoutMediator(binding.myInfoTabLayout,binding.myInfoVp){
            tab, position ->
            tab.text = menu[position]
        }.attach()

        binding.myInfoSettingTv.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }


        myTrip = MyTripFragment()
        like = LikeFragment()


//        팔로잉,팔로우 화면
        binding.myInfoFollowerTv.setOnClickListener {
            val option: String = "follower"
            followListService.followList(token, userIdx, option)
        }
        binding.myInfoFollowingTv.setOnClickListener {
            val option: String = "following"
           followListService.followList(token, userIdx, option)
        }

//        프로필 수정 화면
        binding.editProfileBtnTv.setOnClickListener {
            profileSettingService = ProfileSettingService()
            profileSettingService.setProfileSettingView(this)
            val token = getJwt(requireContext())
            profileSettingService.profileSetting(token)
        }

        //마이프로필 api



        val currentPage: Int = binding.myInfoVp.currentItem
        search = when (currentPage){
            0 -> "내여행"
            else -> "좋아요"
        }

        myInfoService = MyInfoService()
        myInfoService.setMyInfoView(this)
        myInfoService.myPageInfo(token, search, page)


        return binding.root
    }


    override fun onProfileSettingSuccess(message: String, profile: Profile) {
        LoadingDialog.loadingOff()
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, ProfileEditActivity::class.java)
        intent.putExtra("nickName", profile.nickName)
        intent.putExtra("profileImgUrl", profile.profileImgUrl)
        startActivity(intent)
    }

    override fun onProfileSettingFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        when(code){
            3004 -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            4000, 4001 -> Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProfileSettingLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onMyInfoSuccess(message: String, result: UserMyPageResult,code: Int) {
        LoadingDialog.loadingOff()

        Glide.with(this).load(result.userMyPageInfo.profileImgUrl).into(binding.myInfoProfileImageIv)
        binding.myInfoNicknameTv.text = result.userMyPageInfo.nickName
        binding.myInfoFollowingTv.text = "팔로잉   "+result.userMyPageInfo.followingCount.toString()
        binding.myInfoFollowerTv.text = "팔로워   "+result.userMyPageInfo.followerCount.toString()


        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

    override fun onMyInfoFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when(code){
            3004,3022 -> Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            4000,4001 -> Toast.makeText(requireContext(), "서버오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFollowListSuccess(message: String, result: ArrayList<FollowListResult>, code: Int) {
        LoadingDialog.loadingOff()
        followListResult = result
        when(code){
            1009 -> {
                val intent = Intent(activity, FollowingActivity::class.java).apply {
                    putParcelableArrayListExtra("result", followListResult)
                }
                startActivity(intent)
            }
            1010 -> {
                val intent = Intent(activity, FollowerActivity::class.java).apply {
                    putParcelableArrayListExtra("result", followListResult)
                }
                startActivity(intent)

            }
        }



    }

    override fun onFollowListFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when(code){

            3004, 3005, 3006 -> Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            4000, 4001 ->  Toast.makeText(requireContext(),"서버오류",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFollowListLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onResume() {

        token = getJwt(requireContext())
        search = "내여행"

        myInfoService = MyInfoService()
        myInfoService.setMyInfoView(this)
        myInfoService.myPageInfo(token, search, page)


        super.onResume()

    }

}