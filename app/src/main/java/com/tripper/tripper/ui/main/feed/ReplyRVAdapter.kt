package com.tripper.tripper.ui.main.feed
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.databinding.ItemReplyBinding
import com.tripper.tripper.ui.main.feed.reply.Reply
import com.tripper.tripper.ui.main.feed.searchComment.Comments

class ReplyRVAdapter(val context: Context, private var reply : ArrayList<Reply>?): RecyclerView.Adapter<ReplyRVAdapter.ViewHolder>() {

    interface ReplyItemClickListener{
        fun onReplyThreeDotClick(holder: ViewHolder)
    }

    private lateinit var mItemClickListener: ReplyItemClickListener

    fun setReplyItemClickListener(itemClickListener: ReplyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReplyRVAdapter.ViewHolder {
        val binding: ItemReplyBinding = ItemReplyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reply!![position])
        holder.binding.reply3dotIv.setOnClickListener {
            mItemClickListener.onReplyThreeDotClick(holder)
        }
    }

    override fun getItemCount(): Int = reply!!.size


    inner class ViewHolder(val binding: ItemReplyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(reply: Reply){
            binding.itemReplyReplyEt.setText(reply.comment)
            binding.itemReplyNicknameTv.text = reply.nickName
            Glide.with(binding.itemReplyProfileIv).load(reply.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.itemReplyProfileIv)
        }
    }


    fun setReply(result: ArrayList<Reply>){
        reply = result
        notifyItemInserted(reply!!.size)
    }

    fun resetReply(result: ArrayList<Reply>?){
        reply = result
        notifyItemChanged(0)
    }

}