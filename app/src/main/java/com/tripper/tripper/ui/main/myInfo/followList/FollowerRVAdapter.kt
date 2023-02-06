package com.tripper.tripper.ui.main.myInfo.followList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.databinding.ItemFollowerBinding

class FollowerRVAdapter(var followListResult: ArrayList<FollowListResult>) :
    RecyclerView.Adapter<FollowerRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(followListResult: FollowListResult) {
            binding.followerNicknameTv.text = followListResult.nickName
            Glide.with(binding.followerProfileIv).load(followListResult.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.followerProfileIv)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowerBinding =
            ItemFollowerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followListResult!![position])
        holder.itemView.setOnClickListener { }
    }

    fun followerList(result: ArrayList<FollowListResult>) {

        followListResult = result
        notifyDataSetChanged()
    }

    override fun getItemCount() = followListResult.size

}