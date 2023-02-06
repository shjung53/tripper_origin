package com.tripper.tripper.ui.main.schedule
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.R
import com.tripper.tripper.dataClass.*
import com.tripper.tripper.databinding.ActivityReviewPlaceBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.ui.main.schedule.reviewImage.*
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

class ReviewPlaceActivity : AppCompatActivity(), AddReviewImgView, DeleteReviewImgView {
    var list = ArrayList<Uri?>()
    val adapter = ReviewPlaceImageRVAdapter(list, this)
    private var pageIndex = 0
    private var review = ArrayList<Review>()
    private var reviewImages = ArrayList<String>()

    lateinit var binding: ActivityReviewPlaceBinding

    private var bitmapMultipartBody = ArrayList<MultipartBody.Part>()

    private lateinit var addReviewImgService: AddReviewImgService

    private lateinit var deleteReviewImgService: DeleteReviewImgService

    var keyLocation = ArrayList<ImgKey>()

    var imageCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        val placeName = intent.getStringExtra("placeName")
        binding = ActivityReviewPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addReviewImgService = AddReviewImgService()
        addReviewImgService.setAddReviewImgView(this)

        deleteReviewImgService = DeleteReviewImgService()
        deleteReviewImgService.setDeleteReviewImgView(this)


        review = intent.getParcelableArrayListExtra("Review")!!
        pageIndex = intent.getIntExtra("pageIndex",0)

        val reviewPosition = intent.getIntExtra("reviewPosition",0)

        val jwtToken = getJwt(this)

        val editText = binding.reviewPlaceReviewEt

        val keyLocation = intent.getParcelableArrayListExtra<ImgKey>("keyLocation")

        val uriSize = keyLocation?.size

        if (uriSize != null) {
            if(uriSize > 0)
                for(i in 1 ..uriSize){
                    list.apply {
                        add(keyLocation[i-1].uri)
                    }
                    adapter.items = list
                    adapter.notifyItemChanged(list.size)
                }
        }


        binding.reviewPlaceTopBarTv.text = placeName.toString()
        binding.reviewPlaceTopBarTv.isSelected = true



        // 이미지 불러오기 버튼
        val getImageBtn = binding.reviewPlaceGalleryCl
        // 리사이클러뷰
        val recyclerView = binding.reviewPlaceImgRv
        // 후기 저장 버튼
        val getSaveBtn = binding.reviewPlaceSaveTv


        editText.setText(review[reviewPosition].comment)
        if(editText.length() > 0) {
            getSaveBtn.setBackgroundColor(ContextCompat.getColor(this@ReviewPlaceActivity, R.color.main))
            binding.reviewPlaceSaveTv.isEnabled = true
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter



        binding.reviewPlaceBackIv.setOnClickListener {
            onBackPressed()
        }

        getImageBtn.setOnClickListener {
            TedImagePicker.with(this)
                .max(5, "사진은 최대 5장까지 추가 가능합니다.")
                .mediaType(gun0912.tedimagepicker.builder.type.MediaType.IMAGE)
                .startMultiImage { uriList ->
                    imageCount = list.size + uriList.size
                    if(imageCount <= 5) {
                        adapter.addImage(uriList)
                        binding.reviewPlaceGalleryTv.text = "$imageCount/5"
                        for(i in 1 .. uriList.size){
                            uriList?.let{
                                    uriList ->
                                ToastUtil.context.contentResolver.query(
                                    uriList[i-1],null,null,null,null)
                            }?.use { cursor ->
                                val nameIndex = cursor.getColumnIndex("_data")
                                cursor.moveToFirst()
                                val imgFile = cursor.getString(nameIndex)
                                cursor.close()
                                val test = BitmapFactory.decodeFile(imgFile)
                                val bitmapRequestBody = test?.let { BitmapRequestBody(it) }
                                bitmapMultipartBody.add(MultipartBody.Part.createFormData("travels", imgFile, bitmapRequestBody!!))
                            } }
                    }
                    else Toast.makeText(applicationContext, "사진은 최대 5장까지 추가 가능합니다.", Toast.LENGTH_SHORT).show()
                }
        }


        adapter.setMyItemClickListener(object : ReviewPlaceImageRVAdapter.MyItemClickListener{
            override fun reMoveImg(holder: ReviewPlaceImageRVAdapter.ViewHolder, position: Int) {
                if(keyLocation != null){
                    deleteReviewImgService.deleteImg(jwtToken, keyLocation[holder.adapterPosition].key!!,"travels")
                    keyLocation.removeAt(holder.adapterPosition)
                }
            }

            override fun getSize(size: String) {
                binding.reviewPlaceGalleryTv.text = "$size/5"
            }
        })


//        리뷰 쓰기

        editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트 입력 전
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트 입력 중
            }
            override fun afterTextChanged(p0: Editable?) {
                // 텍스트 입력 후
                if(editText.length() > 0) {
                    getSaveBtn.setBackgroundColor(ContextCompat.getColor(this@ReviewPlaceActivity, R.color.main))
                    binding.reviewPlaceSaveTv.isEnabled = true
                }
                else {
                    getSaveBtn.setBackgroundColor(ContextCompat.getColor(this@ReviewPlaceActivity, R.color.gray))
                    binding.reviewPlaceSaveTv.isEnabled = false
                }
            }
        })




//        리뷰 등록

        binding.reviewPlaceSaveTv.setOnClickListener {
            if(list.size <= 0){
             Toast.makeText(this, "이미지를 최소 1장 등록해 주세요!", Toast.LENGTH_SHORT).show()
            }
            else{
                addReviewImgService.addReviewImg(jwtToken, bitmapMultipartBody)
                val intent = Intent(this,ScheduleActivity::class.java).apply {
                    putExtra("review",binding.reviewPlaceReviewEt.text.toString())
                    putExtra("reviewPosition",reviewPosition)
                    putExtra("pageIndex",pageIndex)
                    putExtra("keyLocation",keyLocation)
                }
                setResult(3,intent)
                finish()
            }
        }
    }





    override fun onAddReviewImgSuccess(message: String,result: ArrayList<ReviewImgInfo>) {
        LoadingDialog.loadingOff()
        val resultSize = result.size
        for(i in 1 .. resultSize){
            reviewImages.apply { add((result[i-1].location)) }
            keyLocation.apply { add(ImgKey(result[i-1].key,list[i-1])) }
        }
        val reviewPosition = intent.getIntExtra("reviewPosition",0)
        review[reviewPosition].images = reviewImages
        val intent = Intent(this,ScheduleActivity::class.java).apply {
            putExtra("reviewImages",reviewImages)
            putExtra("review",binding.reviewPlaceReviewEt.text.toString())
            putExtra("reviewPosition",reviewPosition)
            putExtra("pageIndex",pageIndex)
            putExtra("keyLocation",keyLocation)
        }
        setResult(2,intent)
        finish()
    }

    override fun onAddReviewImgFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onAddReviewImgLoading() {
        LoadingDialog.loadingOn(this)
    }


    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): okhttp3.MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 33, sink.outputStream())
        }
    }

    override fun onDeleteReviewImgSuccess(message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onDeleteReviewImgFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this,code.toString() + message,Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteReviewImgLoading() {
        LoadingDialog.loadingOn(this)
    }

}

