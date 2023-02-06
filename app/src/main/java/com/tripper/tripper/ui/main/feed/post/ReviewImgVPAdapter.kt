package com.tripper.tripper.ui.main.feed.post

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReviewImgVPAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val reviewImg: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = reviewImg.size

    override fun createFragment(position: Int): Fragment = reviewImg[position]

    fun addFragment (fragment: Fragment) {
        reviewImg.add(fragment)
        notifyItemInserted(reviewImg.size - 1)
    }
}