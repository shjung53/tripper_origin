package com.tripper.tripper.ui.main.schedule.addPlace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ItemAddPlaceBinding

class AddPlaceRVAdapter(var searchKeyWordData: ArrayList<SearchKeyWordData>) : RecyclerView.Adapter<AddPlaceRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun addPlace(holder: ViewHolder, position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener  = itemClickListener
    }


    inner class ViewHolder(val binding: ItemAddPlaceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(searchKeyWordData : SearchKeyWordData){

            binding.addPlaceRvSpaceNameTv.text= searchKeyWordData.place_name
            binding.addPlaceRvSpaceAddressTv.text=searchKeyWordData.address_name
            binding.addPlaceRvSpaceAddressTv.isSelected = true
            binding.addPlaceRvSpaceNameTv.isSelected = true
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemAddPlaceBinding = ItemAddPlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchKeyWordData[position])
        holder.binding.addPlaceRvIconIv.setImageResource(
            if(searchKeyWordData[holder.adapterPosition].category_code== "AT4") {
            R.drawable.ic_landscape_merge
        }else if (searchKeyWordData[holder.adapterPosition].category_code == "AD5"){
            R.drawable.ic_bed_merge
        }else if (searchKeyWordData[holder.adapterPosition].category_code == "FD6" || searchKeyWordData[holder.adapterPosition].category_code == "CE7"){
            R.drawable.ic_food_merge
        }else if(searchKeyWordData[holder.adapterPosition].category_code == null){
            R.drawable.ic_etc_37dp
        }else {
            R.drawable.ic_etc_37dp })
        holder.binding.itemAddPlaceCl.setOnClickListener {
            mItemClickListener.addPlace(holder, position)
        }
    }



    fun addPlace(placeResult: ArrayList<SearchKeyWordData>, position: Int){
        searchKeyWordData = placeResult
        notifyItemInserted(position)
    }

    fun resetPlace(placeResult: ArrayList<SearchKeyWordData>){
        searchKeyWordData = placeResult
        notifyDataSetChanged()
    }


    override fun getItemCount() = searchKeyWordData.size
}