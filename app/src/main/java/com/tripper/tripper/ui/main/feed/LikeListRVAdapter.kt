package com.tripper.tripper.ui.main.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.dataClass.LikeListData
import com.tripper.tripper.databinding.ItemLikeListBinding

class LikeListRVAdapter(val context: Context, private val likeList: ArrayList<LikeListData>): RecyclerView.Adapter<LikeListRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLikeListBinding = ItemLikeListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(likeList[position])
    }

    override fun getItemCount(): Int = likeList.size

    inner class ViewHolder(val binding: ItemLikeListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(likeList: LikeListData){
            binding.itemLikeListNicknamTv.text = likeList.nickName
        }
    }
}