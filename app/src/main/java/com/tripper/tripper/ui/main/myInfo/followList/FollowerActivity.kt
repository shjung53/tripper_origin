package com.tripper.tripper.ui.main.myInfo.followList

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.databinding.ActivityFollowerBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.getUserIdx

class FollowerActivity : AppCompatActivity(){
    private lateinit var viewBinding: ActivityFollowerBinding



    private lateinit var followListResult : ArrayList<FollowListResult>
    private lateinit var followerRVAdapter : FollowerRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        followListResult = intent.getParcelableArrayListExtra("result")!!
        Log.d("넘어온 값", followListResult.toString())



        viewBinding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)





        viewBinding.followerBackIv.setOnClickListener {
            finish()
        }


        followerRVAdapter = FollowerRVAdapter(followListResult)

        viewBinding.followerRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.followerRv.adapter = followerRVAdapter

        followerRVAdapter.notifyDataSetChanged()

        setContentView(viewBinding.root)

    }

}