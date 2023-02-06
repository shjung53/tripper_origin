package com.tripper.tripper
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog

object LoadingDialog {
    lateinit var loadingDialog : AppCompatDialog

    fun loadingOn(context: Context) {
        loadingDialog = AppCompatDialog(context)
        loadingDialog.setCancelable(false)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.show()
            val loadingImg = loadingDialog.findViewById<ImageView>(R.id.dialog_loading_iv)
            val animation: Animation = AnimationUtils.loadAnimation(context,R.anim.rotate)
            loadingImg!!.startAnimation(animation)
    }


    fun loadingOff(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }

}