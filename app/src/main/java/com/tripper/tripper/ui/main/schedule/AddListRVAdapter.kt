package com.tripper.tripper.ui.main.schedule
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.dataClass.MyPlace
import com.tripper.tripper.R
import com.tripper.tripper.dataClass.Review
import com.tripper.tripper.databinding.ItemAddListBinding
import com.tripper.tripper.databinding.ItemAddedListBinding

class AddListRVAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var myPlace = ArrayList<MyPlace>()
    var review = ArrayList<Review>()

    interface MyItemClickListener {
        fun addPlace(holder: ViewHolder0, position: Int)
        fun addReview(holder:ViewHolder1)
        fun removePlace(holder:ViewHolder1)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return myPlace[position].placeType
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemAddList: ItemAddListBinding =
            ItemAddListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val itemAddedList: ItemAddedListBinding =
            ItemAddedListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return when (viewType) {
                type_nothing -> { ViewHolder0(itemAddList) }
                type_food ->{ ViewHolder1(itemAddedList) }
                type_landscape->{ViewHolder1(itemAddedList)}
                type_bed->{ViewHolder1(itemAddedList)}
                type_etc->{ViewHolder1(itemAddedList)}
            else ->{ViewHolder0(itemAddList)}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(myPlace[position].placeType){
            type_nothing->{(holder as ViewHolder0).bind(myPlace[position])
                mItemClickListener.addPlace(holder, position)
            }
            type_food->{(holder as ViewHolder1).bind(myPlace[position])
                holder.binding.itemAddedListCategoryIv.setImageResource(R.drawable.ic_food_merge)
                holder.binding.itemAddedListLineV.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.food_background
                ))
                holder.binding.itemAddedListTv.text = myPlace[holder.adapterPosition].name
                mItemClickListener.addReview(holder)
                mItemClickListener.removePlace(holder)
            }
            type_landscape->{(holder as ViewHolder1).bind(myPlace[position])
                holder.binding.itemAddedListCategoryIv.setImageResource(R.drawable.ic_landscape_merge)
                holder.binding.itemAddedListLineV.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.landscape_background
                ))
                holder.binding.itemAddedListTv.text = myPlace[holder.adapterPosition].name
                mItemClickListener.addReview(holder)
                mItemClickListener.removePlace(holder)
            }
            type_bed->{(holder as ViewHolder1).bind(myPlace[position])
                holder.binding.itemAddedListCategoryIv.setImageResource(R.drawable.ic_bed_merge)
                holder.binding.itemAddedListLineV.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.bed_background
                ))
                holder.binding.itemAddedListTv.text = myPlace[holder.adapterPosition].name
                mItemClickListener.addReview(holder)
                mItemClickListener.removePlace(holder)
            }
            type_etc->{(holder as ViewHolder1).bind(myPlace[position])
                holder.binding.itemAddedListCategoryIv.setImageResource(R.drawable.ic_etc_37dp)
                holder.binding.itemAddedListLineV.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.gray
                ))
                holder.binding.itemAddedListTv.text = myPlace[holder.adapterPosition].name
                mItemClickListener.addReview(holder)
                mItemClickListener.removePlace(holder)
            }
        }
    }


    override fun getItemCount(): Int = myPlace.size

    inner class ViewHolder0(val binding: ItemAddListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myPlace: MyPlace) {

        }
    }

    inner class ViewHolder1(val binding: ItemAddedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myPlace: MyPlace) {
            binding.itemAddedListTv.isSelected = true
        }
    }
}
