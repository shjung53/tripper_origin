package com.tripper.tripper.ui.main.myInfo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemLikeFeedBinding
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageFeedByOption
import gun0912.tedimagepicker.util.ToastUtil

class LikeRVAdapter(private var userMyPageFeedByOption: ArrayList<UserMyPageFeedByOption>): RecyclerView.Adapter<LikeRVAdapter.ViewHolder>() {

    interface  MyItemClickListener{
        fun onHeartClick(holder: ViewHolder)
        fun gotoFeed(holder: ViewHolder)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemLikeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userMyPageFeedByOption: UserMyPageFeedByOption) {
            binding.itemLikeFeedHashtagTv.text = userMyPageFeedByOption.travelHashtag
            Glide.with(binding.itemLikeFeedPictureIv).load(userMyPageFeedByOption.travelThumnail).apply(RequestOptions.centerCropTransform()).into(binding.itemLikeFeedPictureIv)
            //평가
            val rateImg = binding.itemLikeFeedGoodIv
            val rateTv=binding.itemLikeFeedCommentTv

            when(userMyPageFeedByOption.travelScore){

                "최고의 여행!" -> {
                    rateImg.setImageResource(R.drawable.ic_verygood_29dp)
                    rateTv.text="최고의 여행!"
                }
                "도움되었어요!" -> {
                    rateImg.setImageResource(R.drawable.ic_good_29dp)
                    rateTv.text="도움되었어요!"
                }
                "그저 그래요" -> {
                    rateImg.setImageResource(R.drawable.ic_nomal_29dp)
                    rateTv.text="그저 그래요"
                }
                "도움되지 않았어요" -> {
                    rateImg.setImageResource(R.drawable.ic_bad_29dp)
                    rateTv.text="도움되지 않았어요"
                }
                "별로에요" -> {
                    rateImg.setImageResource(R.drawable.ic_verybad_29dp)
                    rateTv.text="별로에요"
                }
                else-> {
                    rateImg.setImageResource(R.drawable.ic_gray_smile)
                    rateTv.text="아직 평점이 없어요"
                    rateTv.setTextColor(ContextCompat.getColor(ToastUtil.context, R.color.darkGray))

                }

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLikeFeedBinding =
            ItemLikeFeedBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userMyPageFeedByOption[position])
        holder.binding.itemLikeFeedTitleTv.text = userMyPageFeedByOption[holder.adapterPosition].travelTitle
        mItemClickListener.onHeartClick(holder)
        holder.binding.itemLikeFeedCv.setOnClickListener {
            mItemClickListener.gotoFeed(holder)
        }
    }


    override fun getItemCount() = userMyPageFeedByOption.size

    fun getFeedInfo(result: ArrayList<UserMyPageFeedByOption>){
        userMyPageFeedByOption = result
        notifyItemInserted(userMyPageFeedByOption.size)

    }

    fun removeLike(result: ArrayList<UserMyPageFeedByOption>, position: Int){
        userMyPageFeedByOption = result
        notifyItemRemoved(position)
    }


}