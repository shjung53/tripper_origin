package com.tripper.tripper.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tripper.tripper.databinding.FragmentBlockBinding

class BlockFragment: Fragment() {

    lateinit var binding: FragmentBlockBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlockBinding.inflate(inflater, container, false)

        return binding.root
    }
}