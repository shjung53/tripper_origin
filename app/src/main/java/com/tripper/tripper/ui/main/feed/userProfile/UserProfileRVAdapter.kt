package com.tripper.tripper.ui.main.feed.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemUserProfileBinding
import gun0912.tedimagepicker.util.ToastUtil.context

class UserProfileRVAdapter(var userProfileList: ArrayList<OtherProfileFeed>) :
    RecyclerView.Adapter<UserProfileRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onHeartClick(holder: ViewHolder)
        fun gotoFeed(holder: ViewHolder)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemUserProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userProfileData: OtherProfileFeed) {
            binding.itemUserProfileTitleTv.text = userProfileData.travelTitle
            Glide.with(binding.itemUserProfilePictureIv).load(userProfileData.thumImgUrl.toString().toUri()).into(binding.itemUserProfilePictureIv)
            binding.itemUserProfileHashtagTv.text = userProfileData.travelHashtag
            // 좋아요
            if (userProfileData.likeStatus == "좋아요 하는중") {
                binding.itemUserProfileHeartIv.setImageResource(R.drawable.ic_emptyheart_white_on_49dp)
            } else {
                binding.itemUserProfileHeartIv.setImageResource(R.drawable.ic_emptyheart_white_49dp)
            }
            // 점수
            when (userProfileData.travelScore) {
                "별로에요" -> {
                    binding.itemUserProfileScoreIv.setImageResource(R.drawable.ic_verybad_29dp)
                    binding.itemUserProfileCommentTv.text = "별로에요"
                    binding.itemUserProfileCommentTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "도움되지 않았어요" -> {
                    binding.itemUserProfileScoreIv.setImageResource(R.drawable.ic_bad_29dp)
                    binding.itemUserProfileCommentTv.text = "도움되지 않았어요"
                    binding.itemUserProfileCommentTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "그저 그래요" -> {
                    binding.itemUserProfileScoreIv.setImageResource(R.drawable.ic_nomal_29dp)
                    binding.itemUserProfileCommentTv.text = "그저 그래요"
                    binding.itemUserProfileCommentTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "도움되었어요!" -> {
                    binding.itemUserProfileScoreIv.setImageResource(R.drawable.ic_good_29dp)
                    binding.itemUserProfileCommentTv.text = "도움되었어요!"
                    binding.itemUserProfileCommentTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "최고의 여행!" -> {
                    binding.itemUserProfileScoreIv.setImageResource(R.drawable.ic_verygood_29dp)
                    binding.itemUserProfileCommentTv.text = "최고의 여행!"
                    binding.itemUserProfileCommentTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemUserProfileBinding =
            ItemUserProfileBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userProfileList[position])
        holder.itemView.setOnClickListener { }
        mItemClickListener.onHeartClick(holder)
        if(userProfileList[holder.adapterPosition].likeStatus == "좋아요 하는중"){
            holder.binding.itemUserProfileHeartIv.setImageResource(R.drawable.ic_emptyheart_white_on_49dp)
        }else {
            holder.binding.itemUserProfileHeartIv.setImageResource(R.drawable.ic_emptyheart_white_49dp)
        }
        holder.binding.itemUserProfileCv.setOnClickListener {
            mItemClickListener.gotoFeed(holder)
        }
    }

    fun userProfile(result: ArrayList<OtherProfileFeed>){
        userProfileList = result
        notifyDataSetChanged()
    }

    override fun getItemCount() = userProfileList.size

    fun setLike(result: ArrayList<OtherProfileFeed>, position: Int){
        userProfileList[position].likeStatus = result[position].likeStatus
        notifyItemChanged(position)
    }
}