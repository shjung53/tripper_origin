package com.tripper.tripper.ui.main.myInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.R
import com.tripper.tripper.databinding.FragmentMyTripBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.feed.post.PostActivity
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.MyInfoService
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.MyInfoView
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageFeedByOption
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.UserMyPageResult
import gun0912.tedimagepicker.util.ToastUtil

class MyTripFragment: Fragment(), ChangeStatusView, DeleteFeedView, MyInfoView {
    lateinit var viewBinding: FragmentMyTripBinding
    private lateinit var changeStatusService: ChangeStatusService
    private lateinit var deleteFeedService: DeleteFeedService
    private lateinit var myInfoService: MyInfoService
    private var userMyPageFeedByOptionResult = ArrayList<UserMyPageFeedByOption>()
    private lateinit var myTripRVAdapter: MyTripRVAdapter
    lateinit var alertDialog: AlertDialog
    var page = 1
    private val myTripViewModel: MyTripViewModel by viewModels()
    var position = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyTripBinding.inflate(inflater,container,false)


        val token = getJwt(ToastUtil.context)
//        Log.d("토큰", token.toString())
        val search: String = "내여행"




        changeStatusService = ChangeStatusService()
        deleteFeedService = DeleteFeedService()
        changeStatusService.setChangeStatusView(this)
        deleteFeedService.setDeleteFeedView(this)
        myInfoService = MyInfoService()
        myInfoService.setMyInfoView(this)
        myInfoService.myPageInfo(token, search, page)


        myTripRVAdapter = MyTripRVAdapter(userMyPageFeedByOptionResult)


        viewBinding.myTripRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        viewBinding.myTripRcv.adapter = myTripRVAdapter

        // 다이얼로그
        val layoutInflater = LayoutInflater.from(activity)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit, null)

        alertDialog = AlertDialog.Builder(requireContext(), R.style.NewDialog)
            .setView(dialogView)
            .create()

        val dialogEditBtn = dialogView.findViewById<CardView>(R.id.dialog_edit_edit_cv) // 수정하기 버튼
        val dialogDeleteBtn = dialogView.findViewById<CardView>(R.id.dialog_edit_delete_cv) // 삭제하기 버튼

        myTripRVAdapter.setMyClickListener(object : MyTripRVAdapter.MyItemClickListener{
            override fun onSwitchClick(holder: MyTripRVAdapter.ViewHolder, position: Int) {
                val travelIdx = userMyPageFeedByOptionResult[holder.adapterPosition].travelIdx
                val travelStatus = userMyPageFeedByOptionResult[holder.adapterPosition].travelStatus

                myTripViewModel.getPosition(holder.adapterPosition)
                travelStatus?.let { myTripViewModel.getStatus(it) }
                        if (travelIdx != null) {
                            changeStatusService.changeStatus(token,travelIdx)
                        }



            }

            override fun onEditClick(holder: MyTripRVAdapter.ViewHolder, position: Int) {
                holder.binding.itemMyTripFeedThreeDotsIv.setOnClickListener {
                    alertDialog.show()
                    dialogDeleteBtn.setOnClickListener {
                        deleteFeedService.deleteFeed(token,userMyPageFeedByOptionResult[holder.adapterPosition].travelIdx!!)
                        myTripViewModel.getPosition(holder.adapterPosition)
                        myTripViewModel.getData(userMyPageFeedByOptionResult)
                    }
                }
            }

            override fun gotoFeed(holder: MyTripRVAdapter.ViewHolder) {
                val intent = Intent(requireContext(), PostActivity::class.java).apply {
                    putExtra("travelIdx",userMyPageFeedByOptionResult[holder.adapterPosition].travelIdx)
                }
                startActivity(intent)
            }
        })

        viewBinding.myTripRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                userMyPageFeedByOptionResult = myTripViewModel.myTripData.value!!
                page = myTripViewModel.page.value!!
                val itemTotalPosition = userMyPageFeedByOptionResult.size - 1
                if (lastVisibleItemPosition == itemTotalPosition && token != null) {
                    myInfoService.myPageInfo(token, search, page)

                }
            }
        })


        return viewBinding.root
    }

    override fun onChangeStatusSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        position = myTripViewModel.position.value!!

        userMyPageFeedByOptionResult = myTripViewModel.myTripData.value!!

        if(userMyPageFeedByOptionResult[position].travelStatus == "공개"){
            userMyPageFeedByOptionResult[position].travelStatus = "비공개"
        }else{userMyPageFeedByOptionResult[position].travelStatus="공개"}

        myTripViewModel.getData(userMyPageFeedByOptionResult)

        myTripRVAdapter.setStatus(userMyPageFeedByOptionResult, position)

        viewBinding.myTripRcv.itemAnimator = null


    }

    override fun onChangeStatusFailure(code: Int, message: String) {
        when (code) {
            4000, 4001 -> Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
        LoadingDialog.loadingOff()
    }

    override fun onChangeStatusLoading() {
        LoadingDialog.loadingOn(requireContext())
    }


    override fun onDeleteFeedSuccess(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        position = myTripViewModel.position.value!!
        userMyPageFeedByOptionResult = myTripViewModel.myTripData.value!!
        userMyPageFeedByOptionResult.removeAt(position)
        myTripViewModel.getData(userMyPageFeedByOptionResult)
        myTripRVAdapter.removeFeed(userMyPageFeedByOptionResult, position)
        alertDialog.dismiss()
        viewBinding.myTripRcv.itemAnimator = null
        LoadingDialog.loadingOff()
    }

    override fun onDeleteFeedFailure(code: Int, message: String) {
        when (code) {
            4000, 4001 -> Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
        LoadingDialog.loadingOff()
    }

    override fun onDeleteFeedLoading() {
        LoadingDialog.loadingOn(requireContext())
    }

    override fun onMyInfoSuccess(message: String, result: UserMyPageResult,code: Int) {
        if(result.userMyPageFeedByOption !=null){

        userMyPageFeedByOptionResult.apply {
            for (i in 1 .. result.userMyPageFeedByOption!!.size){
                add(result.userMyPageFeedByOption!![i-1])
            }
        }
        myTripRVAdapter.getFeedInfo(userMyPageFeedByOptionResult)
        myTripViewModel.getData(userMyPageFeedByOptionResult)
        myTripViewModel.getPage(page+1)
        LoadingDialog.loadingOff()
        }else{

        }
    }

    override fun onMyInfoFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }
}