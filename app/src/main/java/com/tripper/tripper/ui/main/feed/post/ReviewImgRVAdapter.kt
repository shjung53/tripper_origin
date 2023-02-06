package com.tripper.tripper.ui.main.feed.post

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tripper.tripper.databinding.ItemPostListReviewImgBinding
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewData

class ReviewImgRVAdapter(val context: Context, private var reviewImg: ArrayList<String>?):RecyclerView.Adapter<ReviewImgRVAdapter.ViewHolder>() {



    interface MyItemClickListener{
        fun onImgClick()
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener : MyItemClickListener){
        mItemClickListener = itemClickListener
    }




    inner class ViewHolder(val binding: ItemPostListReviewImgBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(reviewImg: String){
            Glide.with(binding.itemPostListReviewImgIv).load(reviewImg).into(binding.itemPostListReviewImgIv)
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemPostListReviewImgBinding = ItemPostListReviewImgBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)
//        binding.itemPostListReviewImgCv.layoutParams = ViewGroup.LayoutParams((viewGroup.width * 0.9).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)


        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(RecyclerView(context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewImg!![position])
        holder.binding.itemPostListReviewImgConstraint.setOnClickListener {
            mItemClickListener.onImgClick()
        }

//        when (position) {
//            0 -> {
//                holder.binding.itemPostListReviewImgConstraint.setPadding(Utils.dpToPx(context, 10f),0,Utils.dpToPx(context, 2.5f),0)
//            }
//            arrayList.size -1 -> {
//                holder.binding.itemPostListReviewImgConstraint.setPadding(Utils.dpToPx(context, 2.5f),0,Utils.dpToPx(context, 10f),0)
//            }
//            else -> {
//                holder.binding.itemPostListReviewImgConstraint.setPadding(Utils.dpToPx(context, 2.5f),0,Utils.dpToPx(context, 2.5f),0)
//            }
//        }
    }

    override fun getItemCount(): Int = reviewImg!!.size

    fun setReviewImg(data: ArrayList<String>?, position: Int){
        reviewImg = data
        notifyDataSetChanged()
    }


}