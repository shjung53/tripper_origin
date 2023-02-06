package com.tripper.tripper.ui.main.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.dataClass.MapFeedData
import com.tripper.tripper.databinding.ItemMapFeedBinding

class MapFeedRVAdapter(private val mapFeedList: ArrayList<MapFeedData>) :
    RecyclerView.Adapter<MapFeedRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMapFeedBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(mapFeedData: MapFeedData) {

                binding.itemMapFeedImgIv.setImageResource(mapFeedData.picture)
                binding.itemMapFeedProfileIv.setImageResource(mapFeedData.profileImg)
                binding.itemMapFeedNicknameTv.text = mapFeedData.nickname
                binding.itemMapFeedTitleTv.text = mapFeedData.title
                binding.itemMapFeedHashtagTv.text = mapFeedData.hashtag
                binding.itemMapFeedRateIv.setImageResource(mapFeedData.rate)
                binding.itemMapFeedRate02Tv.text = mapFeedData.people.toString() + "명의 평가"

            }
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMapFeedBinding =
            ItemMapFeedBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        binding.itemMapFeedCv.layoutParams = ViewGroup.LayoutParams((viewGroup.width * 0.9).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mapFeedList[position])
        holder.itemView.setOnClickListener {}
        if (holder.adapterPosition == 0) {
            holder.binding.itemMapFeedCl.setPadding(50, 0, 15, 0)
        } else if (holder.adapterPosition == mapFeedList.size - 1) {
            holder.binding.itemMapFeedCl.setPadding(15, 0, 50, 0)
        } else {
            holder.binding.itemMapFeedCl.setPadding(15, 0, 15, 0)
        }
    }

    override fun getItemCount() = mapFeedList.size
}