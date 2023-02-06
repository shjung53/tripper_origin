package com.tripper.tripper.ui.main.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemFeedBinding
import com.tripper.tripper.ui.main.home.feedInquiry.FeedData

class HomeFeedRVAdapter(val context: Context, var feedData : ArrayList<FeedData>) : RecyclerView.Adapter<HomeFeedRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onProfileClick(holder: ViewHolder)
        fun onItemClickCV(holder: ViewHolder)
        fun onHeartClick(holder: ViewHolder)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFeedBinding = ItemFeedBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(feedData[position])
        mItemClickListener.onHeartClick(holder)
        if (feedData[holder.adapterPosition].myLikeStatus == "좋아요 하는중"){
            holder.binding.itemFeedHeartOffIv.visibility = View.GONE
            holder.binding.itemFeedHeartOnIv.visibility = View.VISIBLE
        } else {
            holder.binding.itemFeedHeartOffIv.visibility = View.VISIBLE
            holder.binding.itemFeedHeartOnIv.visibility = View.GONE
        }
        holder.binding.itemFeedProfileCiv.setOnClickListener{
            mItemClickListener.onProfileClick(holder)
        }
        holder.binding.itemFeedCv.setOnClickListener{
            mItemClickListener.onItemClickCV(holder)
        }

    }

    override fun getItemCount(): Int = feedData.size


    inner class ViewHolder(val binding: ItemFeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(feedData: FeedData){
            binding.itemFeedTitleTv.text = feedData.title
            binding.itemFeedIdTv.text = feedData.nickName
            Glide.with(binding.itemFeedPictureIv).load(feedData.thumImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.itemFeedPictureIv)
            Glide.with(binding.itemFeedProfileCiv).load(feedData.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.itemFeedProfileCiv)
            binding.itemFeedHashtagTv.text = feedData.travelHashtag
            when (feedData.travelScore) {
                "별로에요" -> {
                    binding.itemFeedScoreIv.setImageResource(R.drawable.ic_verybad_29dp)
                    binding.itemFeedScoreTv.text = "별로에요"
                    binding.itemFeedScoreTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "도움되지 않았어요" -> {
                    binding.itemFeedScoreIv.setImageResource(R.drawable.ic_bad_29dp)
                    binding.itemFeedScoreTv.text = "도움되지 않았어요"
                    binding.itemFeedScoreTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "그저 그래요" -> {
                    binding.itemFeedScoreIv.setImageResource(R.drawable.ic_nomal_29dp)
                    binding.itemFeedScoreTv.text = "그저 그래요"
                    binding.itemFeedScoreTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "도움되었어요!" -> {
                    binding.itemFeedScoreIv.setImageResource(R.drawable.ic_good_29dp)
                    binding.itemFeedScoreTv.text = "도움되었어요!"
                    binding.itemFeedScoreTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
                "최고의 여행!" -> {
                    binding.itemFeedScoreIv.setImageResource(R.drawable.ic_verygood_29dp)
                    binding.itemFeedScoreTv.text = "최고의 여행!"
                    binding.itemFeedScoreTv.setTextColor(ContextCompat.getColor(context, R.color.main))
                }
            }
        }
    }

    fun setLike(result: ArrayList<FeedData>, position: Int){
        feedData[position].myLikeStatus = result[position].myLikeStatus
        notifyItemChanged(position)
    }

    fun getFeed(result: ArrayList<FeedData>){
        feedData = result
        notifyItemInserted(feedData.size)
    }

}
