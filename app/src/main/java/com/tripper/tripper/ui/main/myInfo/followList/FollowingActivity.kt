package com.tripper.tripper.ui.main.myInfo.followList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.ActivityFollowingBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.getUserIdx
import com.tripper.tripper.ui.main.myInfo.follow.FollowService
import com.tripper.tripper.ui.main.myInfo.follow.FollowView
import com.tripper.tripper.ui.main.myInfo.follow.ToIdx

class FollowingActivity : AppCompatActivity(),  FollowView {

    private lateinit var viewBinding: ActivityFollowingBinding

    //팔로우 리스트 조회
    private lateinit var followListService: FollowListService
    private var followListResult = ArrayList<FollowListResult>()
    private lateinit var followingRVAdapter: FollowingRVAdapter
    private var who = ""

    //팔로우 팔로잉
    private lateinit var followService: FollowService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        followListResult = intent.getParcelableArrayListExtra("result")!!
        who = intent.getStringExtra("who").toString()

        viewBinding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val token = getJwt(this)
        val userIdx = getUserIdx(this)
        val option: String = "following"


        //팔로우 팔로잉

        followService = FollowService()
        followService.setFollowView(this)


        viewBinding.followingBackIv.setOnClickListener {
            onBackPressed()
        }

        followingRVAdapter = FollowingRVAdapter(followListResult)




        followingRVAdapter.setMyItemClickListener(object : FollowingRVAdapter.MyItemClickListener {
            override fun followBtn(holder: FollowingRVAdapter.ViewHolder, position: Int) {
                    val index = followingRVAdapter.followingListResult.get(position).toIdx
                    val toIdx = ToIdx(index)

                    followService.follow(token,toIdx)
                    holder.binding.followingFollowingBtn.visibility = View.VISIBLE
                    holder.binding.followingFollowBtn.visibility = View.GONE

            }

            override fun followingBtn(holder: FollowingRVAdapter.ViewHolder, position: Int) {
                val index = followingRVAdapter.followingListResult.get(position).toIdx
                val toIdx = ToIdx(index)
                followService.follow(token,toIdx)

                holder.binding.followingFollowingBtn.visibility = View.GONE
                holder.binding.followingFollowBtn.visibility = View.VISIBLE
            }

            override fun deleteBtn(holder: FollowingRVAdapter.ViewHolder) {
                if(who == "other"){
                    holder.binding.followingFollowingBtn.visibility = View.GONE
                    holder.binding.followingFollowBtn.visibility = View.GONE
                }
            }
        })


        viewBinding.followingRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.followingRv.adapter = followingRVAdapter

        followingRVAdapter.notifyDataSetChanged()

    }



    //팔로우 필로잉

    override fun onFollowSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
}