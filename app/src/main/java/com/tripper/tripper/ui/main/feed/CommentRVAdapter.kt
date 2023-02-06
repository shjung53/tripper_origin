package com.tripper.tripper.ui.main.feed
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.databinding.ItemCommentBinding
import com.tripper.tripper.ui.main.feed.reply.Reply
import com.tripper.tripper.ui.main.feed.searchComment.Comments


class CommentRVAdapter(val context: Context, private var commentsR: ArrayList<Comments>,//ArrayList<CommentsR>, private var allReplies: ArrayList<Reply>?
                             ): RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {
    lateinit var replyRVAdapter : ReplyRVAdapter
    interface MyItemClickListener{
//        fun replyToComment(holder:ViewHolder,position: Int)
        fun onCommentThreeDotClick(holder:ViewHolder)
//        fun onReplyThreeDotClick(holder:ReplyRVAdapter.ViewHolder)
//        fun getReply(holder:ViewHolder)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommentBinding = ItemCommentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(commentsR[position])

//        if(commentsR[holder.adapterPosition].secondCommentCount != null){
//            holder.binding.itemCommentReplyNumTv.text = commentsR[holder.adapterPosition].secondCommentCount.toString()
//        }

        //val replyList = holder.binding.itemCommentReplyRv

//        대댓글 껐다 켜기
//        holder.binding.itemCommentReplyTv.setOnClickListener {
//            mItemClickListener.replyToComment(holder, position)
//            if(replyList.visibility == View.GONE){
//                holder.binding.itemCommentReplyTv.text = "답글닫기"
//                TransitionManager.beginDelayedTransition(replyList, AutoTransition())
//                replyList.visibility = View.VISIBLE
//                Log.d("순서","어댑터")
//            }else if(replyList.visibility == View.VISIBLE){
//                TransitionManager.beginDelayedTransition(replyList, AutoTransition())
//                holder.binding.itemCommentReplyTv.text = "답글달기"
//                replyList.visibility = View.GONE
//            }
//        }

        // 대댓글 조회
//        holder.binding.itemCommentIcIv.setOnClickListener {
//            mItemClickListener.getReply(holder)
//        }

        holder.binding.itemComment3dotIv.setOnClickListener {
            mItemClickListener.onCommentThreeDotClick(holder)
        }


//        val size = commentsR[holder.adapterPosition].secondCommentCount!!
//
//        for(i in 1 .. size){
//            commentsR[holder.adapterPosition].allReplies?.add(Reply(0,0,"","","",""))
//        }
//        Log.d("왜와이", commentsR[holder.adapterPosition].allReplies.toString())
//        replyRVAdapter = ReplyRVAdapter(context,commentsR[holder.adapterPosition].allReplies)
//        //            대댓글 어댑터
//        holder.binding.itemCommentReplyRv.adapter = replyRVAdapter
//        holder.binding.itemCommentReplyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)









//        replyRVAdapter.setReplyItemClickListener(object : ReplyRVAdapter.ReplyItemClickListener{
//            override fun onReplyThreeDotClick(holder: ReplyRVAdapter.ViewHolder) {
//                mItemClickListener.onReplyThreeDotClick(holder)
//            }
//        })

    }


    override fun getItemCount(): Int = commentsR.size


    inner class ViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(commentsR: Comments){
            binding.itemCommentCommentEt.setText(commentsR.comment)
            binding.itemCommentNicknameTv.text = commentsR.nickName
            binding.itemCommentDate.text = commentsR.createdAt + " 전"
            Glide.with(binding.itemCommentProfileIv).load(commentsR.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).into(binding.itemCommentProfileIv)
        }
    }

    fun setComment(result: ArrayList<Comments>){
        commentsR = result
        notifyItemInserted(commentsR.size)
    }

    fun resetComment(result: ArrayList<Comments>){
        commentsR = result
        notifyItemRangeRemoved(0,commentsR.size)
    }

    fun removeComment(result: ArrayList<Comments>, position: Int){
        commentsR = result
        notifyItemRemoved(position)
    }

//    fun setReply(reply: ArrayList<Reply>, position: Int){
//        allReplies = reply
//        replyRVAdapter.resetReply(allReplies)
//    }
}