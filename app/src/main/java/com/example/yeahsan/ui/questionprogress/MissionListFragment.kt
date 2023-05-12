package com.example.yeahsan.ui.questionprogress

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppApplications
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.adapter.MissionFragmentAdapter

import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.databinding.FragmentOutsideMissionProgressBinding
import com.example.yeahsan.ui.popup.HintPopupActivity
import kotlinx.coroutines.*


class MissionListFragment : Fragment() {

    private lateinit var binding: FragmentOutsideMissionProgressBinding
    private var doorList: ArrayList<DoorListVO> = arrayListOf()
    private var clearList: ArrayList<DoorListVO>? = arrayListOf()
    private var type : String ? = null
    //private lateinit var adapter: MissionFragmentAdapter

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

        arguments?.let { bundle ->
            type = bundle.getString(AppConstants.STRING_TYPE)
            activity?.let {

                val baseData = AppDataManager.getInstance(it.application as AppApplication).getBaseData()

                if (type == AppConstants.OUT_DOOR_TYPE) {
                    baseData.let {
                        it?.let {
//                            for(i in 0 until (it.body.outdoorList.size.minus(1))) {
//                                doorList.add((it.body.outdoorList[i]))
//                            }
                            doorList = it.body.outdoorList
                        }
                    }
                    clearList = AppDataManager.getInstance(it.application as AppApplication).getOutdoorMissionClearItems()
                } else if (type == AppConstants.IN_DOOR_TYPE) {
                    baseData.let {
                        it?.let {
//                            for(i in 0 until (it.body.indoorList.size.minus(1))) {
//                                doorList.add((it.body.indoorList[i]))
//                            }
                            doorList = it.body.indoorList
                        }
                    }
                    clearList = AppDataManager.getInstance(it.application as AppApplication).getIndoorMissionClearItems()
                }
            }
            Log.e("TAG","doorList ::: $doorList")
        }
    }


    @SuppressLint("CheckResult")
    private fun initView() {

        activity?.let {activity ->
            clearList?.let { clearList ->
                doorList.let{doorList ->
                    if(clearList.size > 0) {
                        binding.rvQuestList.adapter = MissionFragmentAdapter(activity,doorList,clearList) {
                            val position = it.getTag(R.id.TAG_POSITION) as Int
                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
                               val clearItem = DoorListVO(
                                   doorList[position].seq,
                                   doorList[position].code,
                                   doorList[position].name,
                                   doorList[position].hint,
                                   doorList[position].image,
                                   doorList[position].caption,
                                   doorList[position].thumbnail,
                                   doorList[position].mapX,
                                   doorList[position].mapY,
                                   doorList[position].beaconList,
                                   doorList[position].locationList
                               )

                            val intent = Intent(context,HintPopupActivity::class.java)
                            intent.putExtra(AppConstants.HINT_STRING,doorList[position].hint)
                            intent.putExtra(AppConstants.NAME_STRING,doorList[position].name)
                            intent.putExtra(AppConstants.IMAGE_URL_STRING,doorList[position].image)
                            startActivity(intent)

                            type?.let { type ->
                                AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(clearItem , type)
                            }

                        }
                    } else {
                        binding.rvQuestList.adapter = MissionFragmentAdapter(activity,doorList,null) {
                            val position = it.getTag(R.id.TAG_POSITION) as Int
                            val imageView = it. getTag(R.id.TAG_IV) as ImageView

                            val clearItem = DoorListVO(
                                doorList[position].seq,
                                doorList[position].code,
                                doorList[position].name,
                                doorList[position].hint,
                                doorList[position].image,
                                doorList[position].caption,
                                doorList[position].thumbnail,
                                doorList[position].mapX,
                                doorList[position].mapY,
                                doorList[position].beaconList,
                                doorList[position].locationList)

                            val intent = Intent(context,HintPopupActivity::class.java)
                            intent.putExtra(AppConstants.HINT_STRING,doorList[position].hint)
                            intent.putExtra(AppConstants.NAME_STRING,doorList[position].name)
                            intent.putExtra(AppConstants.IMAGE_URL_STRING,doorList[position].image)
                            startActivity(intent)

                            type?.let { type ->
                                AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(clearItem , type)
                                (activity.application as AppApplication).checkAllClear(type)

                            }
                        }
                    }
                }
            }
        }


/** 리사이클 뷰에 어댑터 먼저 붙이고 데이터 갱신 시켜주는 방식 */
//        activity?.let { activity ->
//            clearList?.let { clearList ->
//                if (clearList.size > 0) {
//                    doorList?.let { list ->
//                        adapter.setParams(activity, list, clearList) {
//                            val position = it.getTag(R.id.TAG_POSITION) as Int
//                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
//                            Glide.with(activity)
//                                .load(R.mipmap.main_vr_phone)
//                                .into(imageView)
//                            val clearItem = DoorListVO(
//                                list[position].seq,
//                                list[position].code,
//                                list[position].name,
//                                list[position].hint,
//                                list[position].image,
//                                list[position].caption,
//                                list[position].thumbnail,
//                                list[position].mapX,
//                                list[position].mapY,
//                                list[position].beaconList,
//                                list[position].locationList
//                            )
//
//                            AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(
//                                clearItem
//                            )
//
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                } else {
//                    doorList?.let { list ->
//                        adapter.setParams(activity, list, null) {
//                            val position = it.getTag(R.id.TAG_POSITION) as Int
//                            val imageView = it.getTag(R.id.TAG_IV) as ImageView
//
//                            Glide.with(activity)
//                                .load(R.mipmap.main_vr_phone)
//                                .into(imageView)
//
//                            val clearItem = DoorListVO(
//                                list[position].seq,
//                                list[position].code,
//                                list[position].name,
//                                list[position].hint,
//                                list[position].image,
//                                list[position].caption,
//                                list[position].thumbnail,
//                                list[position].mapX,
//                                list[position].mapY,
//                                list[position].beaconList,
//                                list[position].locationList
//                            )
//                            AppDataManager.getInstance(activity.application as AppApplication).addMissionClearItem(
//                                clearItem
//                            )
//
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//        }
    }
}
