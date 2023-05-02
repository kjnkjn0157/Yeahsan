package com.example.yeahsan.ui.artifact

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {

    private lateinit var binding : FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCollectionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        getData()
    }

    private fun initView() {

    }

    private fun getData() {

        activity?.let {
            AppDataManager(it.application).getCollectionListData {
                Log.e("TAG","fragment in collection data ::: ${it?.headerVO}")
            }
        }
    }
}