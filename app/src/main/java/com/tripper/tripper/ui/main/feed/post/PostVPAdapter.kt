package com.tripper.tripper.ui.main.feed.post
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PostVPAdapter(fragment: FragmentActivity) :FragmentStateAdapter(fragment){

    private val postList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = postList.size

    override fun createFragment(position: Int): Fragment  = postList[position]

    fun addFragment (fragment: Fragment) {
        postList.add(fragment)
        notifyItemInserted(postList.size - 1)
    }

}