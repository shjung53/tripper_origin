package com.tripper.tripper.ui.main.feed.post

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lriccardo.timelineview.TimelineView
import com.tripper.tripper.R
import com.tripper.tripper.dataClass.PostDayData
import com.tripper.tripper.databinding.ItemPostListRvBinding
import com.tripper.tripper.ui.main.feed.reviewInquiry.ReviewData
import com.tripper.tripper.ui.main.schedule.type_bed
import com.tripper.tripper.ui.main.schedule.type_etc
import com.tripper.tripper.ui.main.schedule.type_food
import com.tripper.tripper.ui.main.schedule.type_landscape
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import java.util.*
import kotlin.collections.ArrayList


class PostListRVAdapter(private val context: Context, private var dayData: ArrayList<PostDayData>,private var allReviews: ArrayList<ReviewData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var reviewImgRVA : ReviewImgRVAdapter

    private var click = false

    interface MyItemClickListener {
        fun itemOpen(holder: ViewHolder)
        fun goReview(holder: ViewHolder)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    override fun getItemViewType(position: Int): Int {
        return dayData[position].placeType
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemPostList: ItemPostListRvBinding =
            ItemPostListRvBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)



        return ViewHolder(itemPostList)


    }


    inner class ViewHolder(val viewBinding: ItemPostListRvBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(dayData: PostDayData) {

//            val reviewImg = allReviews[adapterPosition].travelAreaReviewImage
//            Log.d("이미지",reviewImg.toString())
//
//            if(reviewImg?.size != 0 && click){
//                TransitionManager.beginDelayedTransition(viewBinding.postListRvCloseConstraint, AutoTransition())
//                TransitionManager.beginDelayedTransition(viewBinding.postListOpenCvConstraint, AutoTransition())
//                viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
//                viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
//                viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f
//
//                Glide.with(viewBinding.itemPostListOpenReviewImageIv).load(reviewImg!![0]).optionalFitCenter().into(viewBinding.itemPostListOpenReviewImageIv)
//                click = false
//            }else if(reviewImg?.size == 0 && click){
//                Toast.makeText(context, "리뷰 내용이 없습니다.",Toast.LENGTH_SHORT).show()
//                click = false
//            }

//            reviewImgRVA = ReviewImgRVAdapter(context, reviewImg)
//            viewBinding.itemPostListOpenReviewImageRv.adapter = reviewImgRVA
//            viewBinding.itemPostListOpenReviewImageRv.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


            val name = viewBinding.itemPostListTv
            val nameOpen = viewBinding.itemPostListOpenTv
            val colorVar = viewBinding.itemPostListLineV
            val colorVarOpen = viewBinding.itemPostListOpenLineV
            val categoryImg = viewBinding.itemPostListCategoryIv
            val upBtn = viewBinding.itemPostUpBtnConstraint
            val closeConstraintLayout = viewBinding.postListRvCloseConstraint
            val openConstraintLayout = viewBinding.postListRvOpenConstraint
            val reviewPost = viewBinding.itemPostListOpenReviewTv
            val reviewComment = allReviews[adapterPosition].travelAreaReviewComment


//            name.text = postListRVList.name
//            nameOpen.text = postListRVList.name
            colorVar.setBackgroundResource(R.color.gray)
            colorVarOpen.setBackgroundResource(R.color.gray)
            categoryImg.indicatorDrawable = getDrawable(context, R.drawable.ic_etc_37dp)
            reviewPost.text = reviewComment

//            closeConstraintLayout.setOnClickListener {
//                TransitionManager.beginDelayedTransition(closeConstraintLayout, AutoTransition())
//                TransitionManager.beginDelayedTransition(openConstraintLayout, AutoTransition())
//                openConstraintLayout.visibility = View.VISIBLE
//
//                viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
//                viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f
//            }

            upBtn.setOnClickListener {
                TransitionManager.beginDelayedTransition(closeConstraintLayout, AutoTransition())
                TransitionManager.beginDelayedTransition(openConstraintLayout, AutoTransition())
                openConstraintLayout.visibility = View.GONE
//                categoryImgOpen.visibility = View.INVISIBLE
                viewBinding.postListRvCloseConstraint.visibility = View.VISIBLE
                viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.5f
//                categoryImgClose.visibility = View.VISIBLE
            }

        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (dayData[position].placeType) {
            type_food -> {
                (holder as ViewHolder).bind(dayData[position])
                holder.viewBinding.itemPostListCategoryIv.indicatorDrawable = getDrawable(context, R.drawable.ic_food_merge)
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.food_background))
                holder.viewBinding.itemPostListOpenLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.food_background))
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListOpenTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListCv.setOnClickListener {
                    runBlocking {
                        val reviewImg = allReviews[holder.adapterPosition].travelAreaReviewImage
                        Log.d("클릭", click.toString())

                        click = true
                        mItemClickListener.itemOpen(holder)

                        if (reviewImg?.size != 0 && click) {
                            Log.d("순서111", "111")
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListRvCloseConstraint,
                                AutoTransition()
                            )
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListOpenCvConstraint,
                                AutoTransition()
                            )
                            holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
                            holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
                            holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f

                            Glide.with(holder.viewBinding.itemPostListOpenReviewImageIv)
                                .load(reviewImg!![0]).optionalFitCenter()
                                .into(holder.viewBinding.itemPostListOpenReviewImageIv)
                            click = false
                        } else if (reviewImg?.size == 0 && click) {
                            Toast.makeText(context, "리뷰 내용이 없습니다.", Toast.LENGTH_SHORT).show()
                            click = false
                        }
                    }


//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListRvCloseConstraint, AutoTransition())
//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListOpenCvConstraint, AutoTransition())
//                    holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
//                    holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
//                    holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f


                }
                holder.viewBinding.itemPostListOpenReviewTv.setOnClickListener {
                    mItemClickListener.goReview(holder)
                }

//                reviewImgRVA.setMyClickListener(object : ReviewImgRVAdapter.MyItemClickListener {
//                    override fun onImgClick() {
//                        mItemClickListener.goReview(holder)
//                    }
//                })
            }

            type_landscape -> {
                (holder as ViewHolder).bind(dayData[position])
                holder.viewBinding.itemPostListCategoryIv.indicatorDrawable = getDrawable(context, R.drawable.ic_landscape_merge)
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.landscape_background))
                holder.viewBinding.itemPostListOpenLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.landscape_background))
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListOpenTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListCv.setOnClickListener {
                    runBlocking {
                        val reviewImg = allReviews[holder.adapterPosition].travelAreaReviewImage
                        Log.d("클릭", click.toString())
                        click = true
                        mItemClickListener.itemOpen(holder)

                        if (reviewImg?.size != 0 && click) {
                            Log.d("순서111", "111")
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListRvCloseConstraint,
                                AutoTransition()
                            )
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListOpenCvConstraint,
                                AutoTransition()
                            )
                            holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
                            holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
                            holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f

                            Glide.with(holder.viewBinding.itemPostListOpenReviewImageIv)
                                .load(reviewImg!![0]).optionalFitCenter()
                                .into(holder.viewBinding.itemPostListOpenReviewImageIv)
                            click = false
                        } else if (reviewImg?.size == 0 && click) {
                            Toast.makeText(context, "리뷰 내용이 없습니다.", Toast.LENGTH_SHORT).show()
                            click = false
                        }
//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListRvCloseConstraint, AutoTransition())
//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListOpenCvConstraint, AutoTransition())
//                    holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
//                    holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
//                    holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f
                    }
                }
                holder.viewBinding.itemPostListOpenReviewTv.setOnClickListener {
                    mItemClickListener.goReview(holder)
                }

//                reviewImgRVA.setMyClickListener(object : ReviewImgRVAdapter.MyItemClickListener {
//                    override fun onImgClick() {
//                        mItemClickListener.goReview(holder)
//                    }
//                })
            }

            type_bed -> {
                (holder as ViewHolder).bind(dayData[position])
                holder.viewBinding.itemPostListCategoryIv.indicatorDrawable = getDrawable(context, R.drawable.ic_bed_merge)
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.bed_background))
                holder.viewBinding.itemPostListOpenLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.bed_background))
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListOpenTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListCv.setOnClickListener {
//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListRvCloseConstraint, AutoTransition())
//                    TransitionManager.beginDelayedTransition(holder.viewBinding.postListOpenCvConstraint, AutoTransition())
//                    holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
//                    holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
//                    holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f
                    runBlocking {
                        val reviewImg = allReviews[holder.adapterPosition].travelAreaReviewImage
                        Log.d("클릭", click.toString())
                        click = true
                        mItemClickListener.itemOpen(holder)

                        if (reviewImg?.size != 0 && click) {
                            Log.d("순서111", "111")
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListRvCloseConstraint,
                                AutoTransition()
                            )
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListOpenCvConstraint,
                                AutoTransition()
                            )
                            holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
                            holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
                            holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f

                            Glide.with(holder.viewBinding.itemPostListOpenReviewImageIv)
                                .load(reviewImg!![0]).optionalFitCenter()
                                .into(holder.viewBinding.itemPostListOpenReviewImageIv)
                            click = false
                        } else if (reviewImg?.size == 0 && click) {
                            Toast.makeText(context, "리뷰 내용이 없습니다.", Toast.LENGTH_SHORT).show()
                            click = false
                        }
                    }
                }
                holder.viewBinding.itemPostListOpenReviewTv.setOnClickListener {
                    mItemClickListener.goReview(holder)
                }

//                reviewImgRVA.setMyClickListener(object : ReviewImgRVAdapter.MyItemClickListener {
//                    override fun onImgClick() {
//                        mItemClickListener.goReview(holder)
//                    }
//                })

            }

            type_etc -> {
                (holder as ViewHolder).bind(dayData[position])
                holder.viewBinding.itemPostListCategoryIv.indicatorDrawable = getDrawable(context, R.drawable.ic_etc_37dp)
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
                holder.viewBinding.itemPostListOpenLineV.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
                holder.viewBinding.itemPostListTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListOpenTv.text = dayData[holder.adapterPosition].areaName
                holder.viewBinding.itemPostListCv.setOnClickListener {
                    runBlocking {
                        val reviewImg = allReviews[holder.adapterPosition].travelAreaReviewImage
                        Log.d("클릭", click.toString())

                        click = true
                        mItemClickListener.itemOpen(holder)


                        if (reviewImg?.size != 0 && click) {
                            Log.d("순서111", "111")
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListRvCloseConstraint,
                                AutoTransition()
                            )
                            TransitionManager.beginDelayedTransition(
                                holder.viewBinding.postListOpenCvConstraint,
                                AutoTransition()
                            )
                            holder.viewBinding.postListRvOpenConstraint.visibility = View.VISIBLE
                            holder.viewBinding.postListRvCloseConstraint.visibility = View.INVISIBLE
                            holder.viewBinding.itemPostListCategoryIv.indicatorYPosition = 0.127f

                            Glide.with(holder.viewBinding.itemPostListOpenReviewImageIv)
                                .load(reviewImg!![0]).optionalFitCenter()
                                .into(holder.viewBinding.itemPostListOpenReviewImageIv)
                            click = false
                        } else if (reviewImg?.size == 0 && click) {
                            Toast.makeText(context, "리뷰 내용이 없습니다.", Toast.LENGTH_SHORT).show()
                            click = false
                        }

                    }
                }
                holder.viewBinding.itemPostListOpenReviewTv.setOnClickListener {
                    mItemClickListener.goReview(holder)
                }

//                reviewImgRVA.setMyClickListener(object : ReviewImgRVAdapter.MyItemClickListener {
//                    override fun onImgClick() {
//                        mItemClickListener.goReview(holder)
//                    }
//                })
            }
        }


//        연결선 라이브러리
        if (dayData.size == 1) {
                val viewBinding = (holder as ViewHolder).viewBinding
                val constraintSet = ConstraintSet()
                var layout = ConstraintLayout(context)
            layout = viewBinding.ex
            viewBinding.itemPostListCategoryIv.layoutParams.height =
                ViewGroup.LayoutParams.WRAP_CONTENT

            if (viewBinding.postListRvOpenConstraint.visibility == View.VISIBLE) {
                constraintSet.clone(layout)
                constraintSet.clear(layout.bottom)
//                viewBinding.itemPostListCategoryIv.
//                viewBinding.itemPostListCategoryIv.updateLayoutParams<ConstraintLayout.LayoutParams> {
//                    viewBinding.itemPostListCategoryIv.bottom = ConstraintLayout.LayoutParams.UNSET
//                    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    viewBinding.itemPostListCategoryIv.requestLayout()

            }

        } else {

            when (position) {
                0 -> {
                    (holder as ViewHolder).viewBinding.itemPostListCategoryIv.viewType =
                        TimelineView.ViewType.FIRST
                }
                dayData.size - 1 -> {
                    (holder as ViewHolder).viewBinding.itemPostListCategoryIv.viewType =
                        TimelineView.ViewType.LAST
                }
                else -> {
                    (holder as ViewHolder).viewBinding.itemPostListCategoryIv.viewType =
                        TimelineView.ViewType.MIDDLE
                }
            }
        }


    }


    override fun getItemCount(): Int = dayData.size


    fun setData(data: ArrayList<PostDayData>) {
        dayData = data
        notifyItemInserted(dayData.size)
    }

    fun setReviewData(data: ArrayList<ReviewData>,position: Int){
        allReviews = data
//        reviewImgRVA.setReviewImg(allReviews[position].travelAreaReviewImage, position)
        notifyDataSetChanged()
    }


}



