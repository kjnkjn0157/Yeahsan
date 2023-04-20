package com.example.yeahsan.ui.doormissions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.data.api.model.DoorPathListVO

import com.example.yeahsan.databinding.FragmentMiniMapBinding
import com.example.yeahsan.ui.PopupActivity
import com.witches.mapview.MapView
import com.witches.mapview.`object`.MapMarker
import com.witches.mapview.`object`.MapPath
import com.xw.repo.BubbleSeekBar

class MiniMapFragment : Fragment() {

    private lateinit var binding: FragmentMiniMapBinding
    private lateinit var mContext: Context
    private var markerList : ArrayList<DoorListVO>? = arrayListOf()
    private var pathList : ArrayList<DoorPathListVO>? = arrayListOf()
    private var type : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let {
            type = it.getString(AppConstants.STRING_TYPE)
        }

        context?.let {
            LocalBroadcastManager.getInstance(it.applicationContext).registerReceiver(messageReceiver,
                IntentFilter(AppConstants.INTENT_FILTER_MISSION_ONE_CLEAR)
            )
        }

        binding = FragmentMiniMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type?.let {
            getData(it)
        }

        setQuestProgressbar()
    }

    private fun getData(type : String) {

        activity?.let {
            //api data
            AppDataManager(it.application).getSampleData { list ->
                if(type == AppConstants.OUT_DOOR_TYPE) {
                    markerList = list?.body?.outdoorList
                    pathList = list?.body?.outdoorPathList

                } else if (type == AppConstants.IN_DOOR_TYPE) {
                    markerList = list?.body?.indoorList
                    pathList = list?.body?.indoorPathList
                }
            }
        }
        initView()
    }


    private fun initView() {

        //보여지는 이미지 map 셋팅
        binding.miniMap.setMap(R.drawable.img_map_outdoor)

        //내 위치 사용
        binding.miniMap.enableMyLocationCompass()

        //map zoom 사용 boolean
        binding.miniMap.isZoomEnabled = true

        binding.miniMap.onMapLoadedListener = onMapLoadedListener

        binding.miniMap.onMarkerClickListener = markerClickListener

        setClickEvent()
    }

    private fun setMarker(_mapView: MapView, markerList: ArrayList<DoorListVO>) {

        activity?.let {
            val missionClearList = AppDataManager(it.application).getMissionClearItems()

            missionClearList?.let {
                //클리어한 미션이 하나라도 있을 경우
                if (missionClearList.size > 0) {
                    checkMissionProgress(it ,markerList ,_mapView)
                }
                // 클리어한 미션이 하나도 없을 경우
                else {
                    for (i in markerList.indices) {
                        val marker = MapMarker(context)
                        marker.isClickable =true
                        marker.tag = markerList[i].toString()
                        marker.isTitleVisible = true
                        marker.setPoint(markerList[i].mapX.toFloat(), markerList[i].mapY.toFloat(), false)
                        marker.setMarker(R.mipmap.ico_question_pre)
                        _mapView.addMarker(marker)
                    }
                }
            }
        }
    }

    private fun checkMissionProgress(clearList : ArrayList<DoorListVO> ,markerList : ArrayList<DoorListVO>? ,_mapView: MapView) {

        val allSeq : ArrayList<Int> = arrayListOf()

        clearList.forEach {
            allSeq.add(it.seq)
        }

        markerList?.let {
            for (i in 0 until it.size) {
                val marker = MapMarker(context)
                if (allSeq.contains(it[i].seq)) {
                    marker.setMarker(R.mipmap.ico_clear_marker)
                } else {
                    marker.setMarker(R.mipmap.ico_question_pre)
                }
                marker.isClickable =true
                marker.tag = markerList[i].toString()
                marker.isTitleVisible = true
                marker.setPoint(markerList[i].mapX.toFloat(), markerList[i].mapY.toFloat(), false)
                _mapView.addMarker(marker)
            }
        }
    }


    private fun setPath(_mapView: MapView, pathList : ArrayList<DoorPathListVO>? ) {

        if (activity != null && pathList != null) {

            pathList.forEachIndexed { index, path ->

                val mapPath = MapPath(activity, false)

                mapPath.setStrokeWidth(10)
                mapPath.lineColor = Color.parseColor(path.pathColor)
                mapPath.strokeColor = Color.parseColor(path.strokeColor)

                path.pointList.forEach {
                    mapPath.addPoint(it.pointX.toFloat(), it.pointY.toFloat())
                }

                _mapView.addPath(index, mapPath)
            }
        }
    }


    private val onMapLoadedListener: MapView.OnMapLoadedListener = object :
        MapView.OnMapLoadedListener {
        override fun onMapReady(_mapView: MapView) {

            markerList?.let {
                setMarker(_mapView,it)
            }

            pathList?.let {
                setPath(_mapView,it)
            }
        }

        override fun onMapLoadFailed(mapView: MapView) {
            Log.e("TAG", "onMapLoadFailed ::: ")
        }

        override fun onRouteReady(p0: MapView?) {

        }
    }

    private fun setQuestProgressbar() {

        binding.seekBar.setProgress(0.0f) //퀘스트 성공 할 때마다 변경된 값 가져와 셋팅 && prefs 에 데이터 저장돼 있는 값이 있는지 선행조건으로

        binding.seekBar.isEnabled = false

//        binding.seekBar.onProgressChangedListener =
//            object : BubbleSeekBar.OnProgressChangedListener {
//                override fun onProgressChanged(
//                    bubbleSeekBar: BubbleSeekBar?,
//                    progress: Int,
//                    progressFloat: Float,
//                    fromUser: Boolean
//                ) {
//
//                }
//
//                override fun getProgressOnActionUp(
//                    bubbleSeekBar: BubbleSeekBar?,
//                    progress: Int,
//                    progressFloat: Float
//                ) {
//
//                }
//
//                override fun getProgressOnFinally(
//                    bubbleSeekBar: BubbleSeekBar?,
//                    progress: Int,
//                    progressFloat: Float,
//                    fromUser: Boolean
//                ) {
//
//                }
//            }
    }


    private fun setClickEvent() {

        binding.btnMapQuest.setOnClickListener {
            getQuestScore("Y")
        }

        binding.btnHome.setOnClickListener {
            activity?.finish()
        }

        binding.btnSearchQuestion.setOnClickListener {

        }
    }

    private fun getQuestScore(result: String) {

        if (result == "Y" && binding.seekBar.progress < 100) {

            binding.seekBar.setProgress(binding.seekBar.progress + 12.5f)
        }
    }


    private val markerClickListener = object :MapView.OnMarkerClickListener {
        override fun onMarkerClick(marker: MapMarker?) {
            Log.e("TAG","onMarkerCLick:::")
            //todo 필요 데이터 intent 에 담아 같이 보내기
            val intent = Intent(context ,PopupActivity::class.java)
            startActivity(intent)
        }

        override fun onMarkersClick(marker: java.util.ArrayList<MapMarker>?) {
            Log.e("TAG","onMarkersClick:::")
        }

    }

    private val messageReceiver : BroadcastReceiver = object :BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            initView()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.miniMap.onResume()
    }

    override fun onPause() {
        super.onPause()

        binding.miniMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.miniMap.onDestroy()
        context?.let {
            LocalBroadcastManager.getInstance(it.applicationContext)
                .unregisterReceiver(messageReceiver)
        }
    }
}