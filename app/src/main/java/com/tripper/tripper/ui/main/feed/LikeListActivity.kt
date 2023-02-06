package com.tripper.tripper.ui.main.feed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.dataClass.LikeListData
import com.tripper.tripper.databinding.ActivityLikeListBinding

class LikeListActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLikeListBinding

    private var likeList = ArrayList<LikeListData>()

    private lateinit var likeListRVAdapter: LikeListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.likeListIcBackIv.setOnClickListener {
            onBackPressed()
        }

        likeListRVAdapter = LikeListRVAdapter(this, likeList)
        binding.likeListRv.adapter = likeListRVAdapter
        binding.likeListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        likeList.apply {
            add(LikeListData("메이"))
            add(LikeListData("사자"))
            add(LikeListData("하제"))
            add(LikeListData("제이슨"))
        }

    }
}