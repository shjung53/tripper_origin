package com.tripper.tripper.ui.main.schedule
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tripper.tripper.databinding.ItemReviewPlaceImgBinding

class ReviewPlaceImageRVAdapter(var items: ArrayList<Uri?>, val context: Context) :
    RecyclerView.Adapter<ReviewPlaceImageRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun reMoveImg(holder: ViewHolder, position: Int)
        fun getSize(size: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener


    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }




    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context).load(item)
            .override(500, 500)
            .into(holder.image)
        holder.binding.itemReviewXIv.setOnClickListener {
            mItemClickListener.reMoveImg(holder, position)
            removeImg(holder.adapterPosition)
            mItemClickListener.getSize(items.size.toString())
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemReviewPlaceImgBinding =
            ItemReviewPlaceImgBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ItemReviewPlaceImgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var image = binding.itemReviewImgIv
    }

    fun addImage(list: List<Uri>) {
        val count = itemCount
        items.addAll(list)
        notifyItemRangeInserted(count, list.size)
    }

    private fun removeImg(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}