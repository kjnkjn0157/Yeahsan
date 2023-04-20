package com.example.yeahsan.ui.questionprogress

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.yeahsan.R
import com.example.yeahsan.adapter.MissionFragmentAdapter
import com.example.yeahsan.data.AppDataHelper

import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.databinding.FragmentOutsideMissionProgressBinding
import kotlinx.coroutines.*


class OutsideMissionFragment : Fragment() {

    private lateinit var binding: FragmentOutsideMissionProgressBinding
    private var outdoorList: ArrayList<DoorListVO>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOutsideMissionProgressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        startView()
    }

    private fun startView() = CoroutineScope(Dispatchers.Main).launch {
        delay(100L)

        initView()
    }

    private fun getData() {

        activity?.let {
            AppDataManager(it.application).getSampleData { list ->
                outdoorList = list?.body?.outdoorList
            }
        }

    }

    private fun initView() {

        outdoorList?.let { list->
            binding.rvQuestList.adapter = context?.let { context ->
                MissionFragmentAdapter(context,list) {
                    //test code--> click 시 미션 성공이라 가장하고 진행
                    val position = it.getTag(R.id.TAG_POSITION) as Int
                    val imageView = it.getTag(R.id.TAG_IV) as ImageView
                    Glide.with(context)
                        .load(R.mipmap.main_vr_phone)
                        .into(imageView)

                    val clearItem = DoorListVO(
                        list[position].seq,
                        list[position].code,
                        list[position].name,
                        list[position].hint,
                        list[position].image,
                        list[position].mapX,
                        list[position].mapY,
                        list[position].beaconList
                    )
                   activity?.let {activity ->
                       AppDataManager(activity.application).addMissionClearItem(clearItem)
                   }
                }
            }
        }
    }

}