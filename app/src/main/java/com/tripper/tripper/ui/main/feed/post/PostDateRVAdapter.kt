package com.tripper.tripper.ui.main.feed.post
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemScheduleDateBinding
import gun0912.tedimagepicker.util.ToastUtil

class PostDateRVAdapter(val context: Context, var dateIndex: Int, var dayIdx: ArrayList<Int>, var dates: ArrayList<String>): RecyclerView.Adapter<PostDateRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun movePage(holder: ViewHolder,position: Int)
        fun selectMove(holder: ViewHolder)
    }

    private lateinit var mItemClickListener: MyItemClickListener


    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener  = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemScheduleDateBinding = ItemScheduleDateBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayIdx[position])
        setDate(holder)
        mItemClickListener.selectMove(holder)
        holder.itemView.setOnClickListener {
            mItemClickListener.movePage(holder, position)
        }
    }

    private fun setDate(holder: ViewHolder) {
        if (dateIndex == holder.adapterPosition) {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector)
            holder.binding.itemScheduleDateTv1.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.itemScheduleDateTv2.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector2)
            holder.binding.itemScheduleDateTv1.setTextColor(ContextCompat.getColor(context, R.color.lightBlack))
            holder.binding.itemScheduleDateTv2.setTextColor(ContextCompat.getColor(context, R.color.lightBlack))
        }
    }

    override fun getItemCount(): Int = dayIdx.size


    inner class ViewHolder(val binding: ItemScheduleDateBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dayIndex: Int){
            binding.itemScheduleDateTv1.text = "Day ${adapterPosition+1}"
            binding.itemScheduleDateTv2.text = dates[adapterPosition]
        }
    }

    fun setDate(dateIdx: ArrayList<Int>){
        dayIdx = dateIdx
        notifyItemInserted(dayIdx.size)
    }

    fun setDateText(result: ArrayList<String>){
        dates = result
        notifyItemInserted(result.size)
    }
}