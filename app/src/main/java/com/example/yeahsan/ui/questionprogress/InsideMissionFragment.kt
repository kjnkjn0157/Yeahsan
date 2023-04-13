package com.example.yeahsan.ui.questionprogress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.R
import com.example.yeahsan.databinding.FragmentInsideMissionBinding

class InsideMissionFragment : Fragment() {

    private lateinit var binding : FragmentInsideMissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInsideMissionBinding.inflate(inflater,container,false)
        return binding.root
    }

}