package com.tripper.tripper.ui.main.feed.post

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ActivityReviewContentBinding
import com.rd.PageIndicatorView
import com.tripper.tripper.ui.main.feed.post.PostVPAdapter
import com.tripper.tripper.ui.main.feed.post.PostVPFragment

class ReviewContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val comment = intent.getStringExtra("reviewComment")
        val reviewImg = intent.getStringArrayListExtra("reviewImg")
        val placeName = intent.getStringExtra("placeName")



        binding = ActivityReviewContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reviewContentBackIv.setOnClickListener {
            onBackPressed()
        }

        val reviewVPAdapter = PostVPAdapter(this)

        binding.reviewContentVp.adapter = reviewVPAdapter
        binding.reviewContentVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val vpIndicatorView: PageIndicatorView = binding.reviewContentVpIndicator
        vpIndicatorView.count = reviewVPAdapter.itemCount

        for(i in 1 .. reviewImg!!.size){
            reviewVPAdapter.addFragment(PostVPFragment(reviewImg[i-1]))
        }
        binding.reviewContentTv.text = comment
        binding.reviewContentTopBarTv.text = placeName

        binding.reviewContentVp.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vpIndicatorView.selection = position
            }
        }
        )
    }
}