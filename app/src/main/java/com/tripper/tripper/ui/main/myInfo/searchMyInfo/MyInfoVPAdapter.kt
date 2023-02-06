package com.tripper.tripper.ui.main.myInfo.searchMyInfo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tripper.tripper.ui.main.myInfo.LikeFragment
import com.tripper.tripper.ui.main.myInfo.MyTripFragment

class
MyInfoVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyTripFragment()
            else -> LikeFragment()
        }

    }
}