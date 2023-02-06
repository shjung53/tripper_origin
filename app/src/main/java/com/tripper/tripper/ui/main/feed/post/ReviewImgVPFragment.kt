package com.tripper.tripper.ui.main.feed.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tripper.tripper.databinding.FragmentReviewImgBinding

class ReviewImgVPFragment(private val reviewImg: String) : Fragment() {

    lateinit var binding : FragmentReviewImgBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewImgBinding.inflate(inflater, container, false)

        Glide.with(binding.fragmentReviewImg).load(reviewImg).into(binding.fragmentReviewImg)

        return binding.root

    }
}