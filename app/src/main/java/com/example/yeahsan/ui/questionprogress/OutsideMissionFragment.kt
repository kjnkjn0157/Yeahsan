package com.example.yeahsan.ui.questionprogress

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.yeahsan.AppApplication
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
    private var clearList: ArrayList<DoorListVO>? = arrayListOf()
    private lateinit var adapter: MissionFragmentAdapter

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

            val temp = AppDataManager.getInstance(it.application as AppApplication).getBaseData()
            temp.let {
                outdoorList = it?.body?.outdoorList
            }
            clearList = AppDataManager.getInstance(it.application as AppApplication).getMissionClearItems()
            adapter = MissionFragmentAdapter(it, arrayListOf(), arrayListOf(), null)
        }

        binding.rvQuestList.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {

//        clearList?.let { clearList ->
//            if (clearList.size > 0) {
//                outdoorList?.let { list ->
//                    binding.rvQuestList.adapter =
//                        context?.let { context ->
//                            MissionFragmentAdapter(context, list, clearList) {
//                                //test code--> click 시 미션 성공이라 가장하고 진행
//                                val position = it.getTag(R.id.TAG_POSITION) as Int
//                                val imageView = it.getTag(R.id.TAG_IV) as ImageView
//                                Glide.with(context)
//                                    .load(R.mipmap.main_vr_phone)
//                                    .into(imageView)
//
//                                val clearItem = DoorListVO(
//                                    list[position].seq,
//                                    list[position].code,
//                                    list[position].name,
//                                    list[position].hint,
//                                    list[position].image,
//                                    list[position].mapX,
//                                    list[position].mapY,
//                                    list[position].beaconList,
//                                    list[position].locationList
//                                )
//                                activity?.let { activity ->
//                                    AppDataManager(activity.application).addMissionClearItem(
//                                        clearItem
//                                    )
//                                }
//                            }
//                        }
//                }
//            } else {
//                outdoorList?.let { list ->
//                    binding.rvQuestList.adapter = context?.let { context ->
//                        MissionFragmentAdapter(context, list, null) {
//                            //test code--> click 시 미션 성공이라 가장하고 진행
//                            val position = it.getTag(R.id.TAG_POSITION) as Int
//                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
//                            Glide.with(context)
//                                .load(R.mipmap.main_vr_phone)
//                                .into(imageView)
//
//                            val clearItem = DoorListVO(
//                                list[position].seq,
//                                list[position].code,
//                                list[position].name,
//                                list[position].hint,
//                                list[position].image,
//                                list[position].mapX,
//                                list[position].mapY,
//                                list[position].beaconList,
//                                list[position].locationList
//                            )
//                            activity?.let { activity ->
//                                AppDataManager(activity.application).addMissionClearItem(
//                                    clearItem
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }

        activity?.let { activity ->
            clearList?.let { clearList ->
                if (clearList.size > 0) {
                    outdoorList?.let { list ->
                        Log.e("TAG", "outdoorList:::")
                        adapter.setParams(activity, list, clearList) {
                            Log.e("TAG", "setParams:::")
                            val position = it.getTag(R.id.TAG_POSITION) as Int
                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
                            Glide.with(activity)
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
                                list[position].beaconList,
                                list[position].locationList
                            )

                            AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(
                                clearItem
                            )

                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    outdoorList?.let { list ->
                        adapter.setParams(activity, list, null) {
                            val position = it.getTag(R.id.TAG_POSITION) as Int
                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
                            Glide.with(activity)
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
                                list[position].beaconList,
                                list[position].locationList
                            )
                            AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(
                                clearItem
                            )

                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}
