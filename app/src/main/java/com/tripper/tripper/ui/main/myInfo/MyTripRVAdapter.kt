package com.tripper.tripper.ui.main.myInfo


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.convertTo
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemMyTripFeedBinding
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageFeedByOption
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageResult
import gun0912.tedimagepicker.util.ToastUtil.context
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class MyTripRVAdapter(private var userMyPageFeedByOption: ArrayList<UserMyPageFeedByOption>): RecyclerView.Adapter<MyTripRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onSwitchClick(holder: ViewHolder, position: Int)
        fun onEditClick(holder: ViewHolder, position: Int)
        fun gotoFeed(holder: ViewHolder)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemMyTripFeedBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ResourceAsColor")
        fun bind(userMyPageFeedByOption: UserMyPageFeedByOption){

            binding.itemMyTripFeedTitleTv.text = userMyPageFeedByOption.travelTitle
            binding.itemMyTripFeedHashtagTv.text = userMyPageFeedByOption.travelHashtag
            Glide.with(binding.itemMyTripFeedPictureIv).load(userMyPageFeedByOption.travelThumnail).apply(RequestOptions.centerCropTransform()).into(binding.itemMyTripFeedPictureIv)

            //평가
            val rateImg = binding.itemMyTripFeedGoodIv
            val rateTv=binding.itemMyTripFeedCommentTv

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
                    rateTv.setTextColor(ContextCompat.getColor(context, R.color.darkGray))

                }

            }

//            //토글 공개, 비공개
            val toggleSwitch = binding.myTripSwitch
            val myTripSwitchTv = binding.myTripSwitchTv
            when (userMyPageFeedByOption.travelStatus){

                "공개" -> {
                    userMyPageFeedByOption.travelStatus = "공개"
                    toggleSwitch.isChecked = true
                    myTripSwitchTv.text = "공개"
                }
                else -> {
                    userMyPageFeedByOption.travelStatus = "비공개"
                    toggleSwitch.isChecked = false
                    myTripSwitchTv.text = "비공개"
                }
            }


        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMyTripFeedBinding = ItemMyTripFeedBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userMyPageFeedByOption[position])

        when(userMyPageFeedByOption.get(position).travelStatus){
            "공개" -> {
                holder.binding.myTripSwitch.isChecked = true
                holder.binding.myTripSwitchTv.text = "공개"
            }
            else -> {
                holder.binding.myTripSwitch.isChecked = false
                holder.binding.myTripSwitchTv.text = "비공개"
            }
        }

        holder.binding.myTripSwitch.setOnClickListener {
            mItemClickListener.onSwitchClick(holder, position)
        }

        holder.binding.itemMyTripFeedCv.setOnClickListener {
            mItemClickListener.gotoFeed(holder)
        }
        mItemClickListener.onEditClick(holder, position)

    }

    override fun getItemCount() = userMyPageFeedByOption.size

    fun getFeedInfo(result: ArrayList<UserMyPageFeedByOption>){
        userMyPageFeedByOption = result
        notifyDataSetChanged()
    }

    fun setStatus(result: ArrayList<UserMyPageFeedByOption>, position: Int){
        userMyPageFeedByOption = result
        notifyItemChanged(position)
    }

    fun removeFeed(result: ArrayList<UserMyPageFeedByOption>, position: Int){
        userMyPageFeedByOption = result
        notifyItemRemoved(position)
    }

}