package com.tripper.tripper.ui.setting
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.MainActivity
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ActivityProfileEditBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.nicknameCheck.NicknameService
import com.tripper.tripper.nicknameCheck.NicknameView
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.util.ToastUtil.context
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink

class ProfileEditActivity: AppCompatActivity(), ProfileEditView, NicknameView {

    lateinit var binding: ActivityProfileEditBinding
    private lateinit var profileEditService: ProfileEditService
    private lateinit var nicknameService: NicknameService

    private lateinit var getNickname : String
    private lateinit var profileImg : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getNickname = intent.getStringExtra("nickName").toString()
        profileImg = intent.getStringExtra("profileImgUrl").toString().toUri()

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = getJwt(this)
        var bitmapMultipartBody: MultipartBody.Part? = null

        profileEditService = ProfileEditService()
        profileEditService.setProfileEditView(this)
        nicknameService = NicknameService()
        nicknameService.setNicknameView(this)

        binding.profileEditNicknameEt.setText(getNickname)
        Glide.with(this).load(profileImg).into(binding.profileEditPictureCiv)

        binding.profileEditCameraIv.setOnClickListener{
            TedImagePicker.with(this)
                .mediaType(gun0912.tedimagepicker.builder.type.MediaType.IMAGE)
                .start { uri ->
                    showSingleImage(uri)
                    uri?.let { uri ->
                        context.contentResolver.query(uri, null, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex("_data")
                        cursor.moveToFirst()
                        val imgFile = cursor.getString(nameIndex)
                        cursor.close()
                        val test = BitmapFactory.decodeFile(imgFile)
                        val bitmapRequestBody = test?.let { BitmapRequestBody(it) }
                        bitmapMultipartBody =
                            if (bitmapRequestBody == null) null
                            else MultipartBody.Part.createFormData("profileImage", imgFile, bitmapRequestBody)
                    }
                }

        }

        binding.profileEditDuplicationCheckOffTv.setOnClickListener {
            val tryNickname = binding.profileEditNicknameEt.text.toString()
            nicknameService.nicknameCheck(tryNickname)
        }

        textChangeListener()

        binding.profileEditBackIv.setOnClickListener {
            finish()
        }

        binding.profileEditCompleteTv.setOnClickListener {
            val nickName = binding.profileEditNicknameEt.text.toString()
            val nickNameRequestBody: RequestBody = nickName.toPlainRequestBody()
            val textHashMap = hashMapOf<String, RequestBody>()
            textHashMap["nickName"] = nickNameRequestBody
            profileEditService.profileEdit(token, bitmapMultipartBody, textHashMap)


        }
    }

    private fun showSingleImage(imgUri: Uri) {
        Glide.with(this).load(imgUri).into(binding.profileEditPictureCiv)
    }

    override fun onProfileEditSuccess(message: String) {
        LoadingDialog.loadingOff()
        finish()
    }

    override fun onProfileEditFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            2000,3000,3004 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            4000,4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProfileEditLoading() {
       LoadingDialog.loadingOn(this)
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 33, sink.outputStream())
        }
    }

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

    private fun textChangeListener() {
        binding.profileEditNicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.profileEditDuplicationCheckOffTv.visibility = View.VISIBLE
                binding.profileEditDuplicationCheckOnTv.visibility = View.GONE
                binding.profileEditPossibleTv.visibility = View.GONE
                binding.profileEditNicknameEt.setTextColor(ContextCompat.getColor(context, R.color.lightBlack))
                binding.profileEditCompleteTv.isEnabled = false
                binding.profileEditCompleteTv.setBackgroundColor(ContextCompat.getColor(this@ProfileEditActivity, R.color.gray))
            }

            override fun afterTextChanged(s: Editable?) {
                return }
        })
    }

    override fun onNicknameSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.profileEditDuplicationCheckOffTv.visibility = View.GONE
        binding.profileEditDuplicationCheckOnTv.visibility = View.VISIBLE
        binding.profileEditPossibleTv.visibility = View.VISIBLE
        binding.profileEditErrorTv.visibility = View.GONE
        binding.profileEditCompleteTv.setBackgroundColor(ContextCompat.getColor(this, R.color.main))
        binding.profileEditCompleteTv.isEnabled = true
    }

    override fun onNicknameFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        when (code) {
            4000,4001 -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()
        }
        if (binding.profileEditNicknameEt.text.toString() == getNickname) {
            binding.profileEditDuplicationCheckOffTv.visibility = View.GONE
            binding.profileEditDuplicationCheckOnTv.visibility = View.VISIBLE
            binding.profileEditPossibleTv.visibility = View.VISIBLE
            binding.profileEditErrorTv.visibility = View.GONE
            binding.profileEditCompleteTv.setBackgroundColor(ContextCompat.getColor(this, R.color.main))
            binding.profileEditCompleteTv.isEnabled = true
            Toast.makeText(this, "이전 닉네임과 동일합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            when (code) {
                2001 -> {Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    binding.profileEditErrorTv.text = message
                }
                2002 -> binding.profileEditErrorTv.text = "닉네임 형식이 잘못되었습니다.\n(한글, 영어, 숫자, 포함 2자 이상, 10자 이하)"
                3001 -> binding.profileEditErrorTv.text = message
            }
            binding.profileEditNicknameEt.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.profileEditErrorTv.visibility = View.VISIBLE
            binding.profileEditPossibleTv.visibility = View.GONE


        }
    }

    override fun onNicknameLoading() {
        LoadingDialog.loadingOn(this)
    }

}