package com.tripper.tripper.ui.main.myInfo.followList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.databinding.ItemFollowingBinding

class FollowingRVAdapter(var followingListResult: ArrayList<FollowListResult>) :
    RecyclerView.Adapter<FollowingRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun followBtn(holder: ViewHolder, position: Int)
        fun followingBtn(holder: ViewHolder, position: Int)
        fun deleteBtn(holder: ViewHolder)

    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    inner class ViewHolder(val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val followingBtn = binding.followingFollowingBtn
        val followBtn = binding.followingFollowBtn


        fun bind(followingList: FollowListResult) {
            binding.followingNicknamTv.text = followingList.nickName
            Glide.with(binding.followingProfileIv).load(followingList.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.followingProfileIv)

        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowingBinding =
            ItemFollowingBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        mItemClickListener.deleteBtn(holder)

        holder.bind(followingListResult[position])

        holder.followingBtn.setOnClickListener {

            mItemClickListener.followingBtn(holder, position)

        }
        holder.followBtn.setOnClickListener {

            mItemClickListener.followBtn(holder, position)

        }
    }

    fun followingList(result: ArrayList<FollowListResult>) {
        followingListResult = result
        notifyDataSetChanged()
    }


    override fun getItemCount() = followingListResult.size

}