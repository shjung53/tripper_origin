package com.tripper.tripper.ui.main.schedule
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.dataClass.*
import com.tripper.tripper.databinding.FragmentScheduleListBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.schedule.addPlace.AddPlaceActivity
import com.tripper.tripper.ui.main.schedule.reviewImage.DeleteReviewImgService
import com.tripper.tripper.ui.main.schedule.reviewImage.DeleteReviewImgView
import kotlinx.coroutines.*
import kotlin.collections.ArrayList


class ScheduleListFragment : Fragment(), DeleteReviewImgView {

    lateinit var binding: FragmentScheduleListBinding

    private lateinit var getResult: ActivityResultLauncher<Intent>

    private lateinit var getReview: ActivityResultLauncher<Intent>

    var myPlace = ArrayList<MyPlace>()

    private var itemPosition = 0

    private var reviewPosition = 0

    var review = ArrayList<Review>()

    private var images = ArrayList<String>()

    var area = ArrayList<Area>()

    var pageIndex = 0

    private var allReviewImgKey = ArrayList<ReviewImgKey>()

    private var reviewImgKey = ArrayList<KeyLocation>()

    private var keyLocation = ArrayList<ImgKey>()


    private val dayIndexViewModel : DayIndexViewModel by activityViewModels()
    private val dayViewModel : DayViewModel by activityViewModels()
    private val reviewImgKeyModel: ReviewImgKeyModel by activityViewModels()

    var day = ArrayList<Day>()

    private lateinit var deleteReviewImgService: DeleteReviewImgService


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleListBinding.inflate(inflater, container, false)

        day = dayViewModel.day.value!!

        allReviewImgKey = reviewImgKeyModel.allReviewImgKey.value!!

        deleteReviewImgService = DeleteReviewImgService()
        deleteReviewImgService.setDeleteReviewImgView(this)





//        장소리스트 추가 어댑터
        val addListRVAdapter = AddListRVAdapter(requireContext())

        binding.scheduleAddListRv.adapter = addListRVAdapter

        binding.scheduleAddListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)




//        액티비티에서 데이터 콜백
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == 1){
                myPlace = it.data?.getParcelableArrayListExtra("myPlace")!!
                itemPosition = it.data?.getIntExtra("itemPosition",0)!!
                review = it.data?.getParcelableArrayListExtra("Review")!!
                pageIndex = it.data?.getIntExtra("pageIndex",0)!!
                area = it.data?.getParcelableArrayListExtra("Area")!!
                reviewImgKey = it.data?.getParcelableArrayListExtra("reviewImgKey")!!
                allReviewImgKey[pageIndex].reviewImgKey = reviewImgKey
                reviewImgKeyModel.getKey(allReviewImgKey)
                day[pageIndex].area = area
                dayViewModel.getDay(day)
                addListRVAdapter.myPlace = myPlace
                addListRVAdapter.notifyItemInserted(itemPosition)
            }
        }


        getReview = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val comment: String
            if(it.resultCode == 2){
                pageIndex = it.data?.getIntExtra("pageIndex",0)!!
                images = it.data?.getStringArrayListExtra("reviewImages")!!
                reviewPosition = it.data?.getIntExtra("reviewPosition",0)!!
                comment = it.data?.getStringExtra("review").toString()
                review[reviewPosition].images = images
                review[reviewPosition].comment = comment
                area[reviewPosition].review = review[reviewPosition]
                day[pageIndex].area = area
                dayViewModel.getDay(day)
                keyLocation = it.data?.getParcelableArrayListExtra("keyLocation")!!
                reviewImgKey[reviewPosition].keyLocation = keyLocation
                allReviewImgKey[pageIndex].reviewImgKey = reviewImgKey
                reviewImgKeyModel.getKey(allReviewImgKey)
            }
            else if (it.resultCode == 3){
                pageIndex = it.data?.getIntExtra("pageIndex",0)!!
                reviewPosition = it.data?.getIntExtra("reviewPosition",0)!!
                comment = it.data?.getStringExtra("review").toString()
                review[reviewPosition].comment = comment
                area[reviewPosition].review = review[reviewPosition]
                day[pageIndex].area = area
                dayViewModel.getDay(day)
                keyLocation = it.data?.getParcelableArrayListExtra("keyLocation")!!
                reviewImgKey[reviewPosition].keyLocation = keyLocation
                allReviewImgKey[pageIndex].reviewImgKey = reviewImgKey
                reviewImgKeyModel.getKey(allReviewImgKey)
            }
        }






//        장소리스트 아이템 클릭 이벤트
        addListRVAdapter.setMyItemClickListener(object : AddListRVAdapter.MyItemClickListener {
            override fun addPlace(holder: AddListRVAdapter.ViewHolder0, position: Int) {
                holder.binding.itemAddListCl.setOnClickListener {
                    pageIndex = dayIndexViewModel.dayIndex.value!!.toInt()
                    val intent = Intent(activity, AddPlaceActivity::class.java)
                    intent.putExtra("pageIndex",pageIndex)
                    intent.putExtra("position",holder.adapterPosition)
                    intent.putParcelableArrayListExtra("myPlace",myPlace)
                    intent.putParcelableArrayListExtra("Review",review)
                    intent.putParcelableArrayListExtra("Area",area)
                    intent.putParcelableArrayListExtra("reviewImgKey",reviewImgKey)
                    getResult.launch(intent)
                }
            }


            override fun addReview(holder: AddListRVAdapter.ViewHolder1) {
                holder.binding.itemAddedListPencilIv.setOnClickListener {
                    val intent = Intent(activity, ReviewPlaceActivity::class.java).apply {
                        pageIndex = dayIndexViewModel.dayIndex.value!!.toInt()
                        putExtra("pageIndex",pageIndex)
                        putExtra("placeName",holder.binding.itemAddedListTv.text.toString())
                        putExtra("reviewPosition",holder.adapterPosition)
                        putExtra("Review",review)
                        putExtra("keyLocation",reviewImgKey[holder.adapterPosition].keyLocation)
                    }
                    getReview.launch(intent)
                }
            }


            override fun removePlace(holder: AddListRVAdapter.ViewHolder1) {
                val jwtToken = getJwt(requireContext())
                holder.binding.itemAddedListMinusIv.setOnClickListener {
                    pageIndex = dayIndexViewModel.dayIndex.value!!.toInt()
                    review.removeAt(holder.adapterPosition)
                    myPlace.removeAt(holder.adapterPosition)
                    area.removeAt(holder.adapterPosition)
                    addListRVAdapter.notifyItemRemoved(holder.adapterPosition)
                    day[pageIndex].area = area
                    if(reviewImgKey[reviewPosition].keyLocation != null){
                        val keyLocationSize = keyLocation.size
                        for(i in 1 .. keyLocationSize){
                            suspend {  deleteReviewImgService.deleteImg(jwtToken, keyLocation[i - 1].key!!, "travels")}
                        }
                        reviewImgKey.removeAt(reviewPosition)
                        allReviewImgKey[pageIndex].reviewImgKey = reviewImgKey
                        reviewImgKeyModel.getKey(allReviewImgKey)
                    }
                }
            }
        })







        //       장소추가 아이템+ 빈 리뷰 추가
        myPlace.apply {
            add(MyPlace(0, "", ""))
        }
        addListRVAdapter.myPlace = myPlace
        addListRVAdapter.notifyItemChanged(myPlace.size-1)



        return binding.root
    }









    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDeleteReviewImgSuccess(message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onDeleteReviewImgFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onDeleteReviewImgLoading() {
        LoadingDialog.loadingOn(requireContext())
    }
}










