package com.tripper.tripper.ui.main.feed.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tripper.tripper.databinding.FragmentPostVpBinding

class PostVPFragment(private val thumbnail: String) : Fragment() {

    lateinit var viewBinding : FragmentPostVpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPostVpBinding.inflate(inflater, container, false)

        Glide.with(viewBinding.postVpIv).load(thumbnail).apply(
            RequestOptions.centerCropTransform()).into(viewBinding.postVpIv)

        return viewBinding.root
    }
}