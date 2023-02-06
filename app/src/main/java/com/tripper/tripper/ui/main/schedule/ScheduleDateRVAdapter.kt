package com.tripper.tripper.ui.main.schedule
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.R
import com.tripper.tripper.dataClass.DatesData
import com.tripper.tripper.databinding.ItemScheduleDateBinding
import gun0912.tedimagepicker.util.ToastUtil

class ScheduleDateRVAdapter(val context: Context, private val dates: ArrayList<DatesData>,var dateIndex: Int): RecyclerView.Adapter<ScheduleDateRVAdapter.ViewHolder>() {

    private val spf = ToastUtil.context.getSharedPreferences("days", AppCompatActivity.MODE_PRIVATE)
    private var days = spf.getLong("days",0)

    interface MyItemClickListener{
        fun movePage(dateIndex: Int)
        fun selectMove()
    }

    private lateinit var mItemClickListener: MyItemClickListener


    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener  = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemScheduleDateBinding = ItemScheduleDateBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleDateRVAdapter.ViewHolder, position: Int) {
        holder.bind(dates[position])
        setDate(holder)
        mItemClickListener.selectMove()
        holder.itemView.setOnClickListener {
            notifyItemChanged(dateIndex)
            dateIndex = holder.adapterPosition
            notifyItemChanged(dateIndex)
            mItemClickListener.movePage(dateIndex)
        }
    }

    private fun setDate(holder: ViewHolder) {
        if (dateIndex == holder.adapterPosition) {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector)
            holder.binding.itemScheduleDateTv1.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.binding.itemScheduleDateTv2.setTextColor(ContextCompat.getColor(context,R.color.white))
        } else {
            holder.binding.itemScheduleDateCv.setBackgroundResource(R.drawable.date_background_selector2)
            holder.binding.itemScheduleDateTv1.setTextColor(ContextCompat.getColor(context,R.color.lightBlack))
            holder.binding.itemScheduleDateTv2.setTextColor(ContextCompat.getColor(context,R.color.lightBlack))
        }
    }

    override fun getItemCount(): Int = days.toInt()


    inner class ViewHolder(val binding: ItemScheduleDateBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(datesData: DatesData){
            binding.itemScheduleDateTv1.text = "Day ${adapterPosition+1}"
            binding.itemScheduleDateTv2.text = datesData.date
            }
        }
    }
