package com.example.yeahsan.ui.doormissions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.FragmentQuestIntroBinding
import com.example.yeahsan.util.OnSingleClickListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer


class QuestIntroFragment : Fragment() {

    private lateinit var binding : FragmentQuestIntroBinding
    private var introUrl = ""
    private var type : String? = null
    private var visibleResult = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            //여기서 인트로 영상 url 가져와서 셋팅
            type = it.getString(AppConstants.STRING_TYPE)
            if (type == AppConstants.IN_DOOR_TYPE) {
                initView(type)
            } else if (type == AppConstants.OUT_DOOR_TYPE) {
                initView(type)
            }
        }

        clickEvent()
    }

    private fun initView(type : String?) {

        activity?.let {activity ->
            type?.let { type ->
                visibleResult = if (type == AppConstants.OUT_DOOR_TYPE) {
                    AppDataManager.getInstance(activity.application as AppApplication).getOutDoorIntroInvisible()
                } else {
                    AppDataManager.getInstance(activity.application as AppApplication).getInDoorIntroInvisible()
                }
            }

        }

        if (visibleResult) {
            val questMapActivity = activity as QuestMapActivity?
            questMapActivity?.onFragmentChange(AppConstants.TYPE_MINIMAP)
        } else {
            context?.let {
                val simpleExoPlayer = SimpleExoPlayer.Builder(it)
                    .build()
                    .also{exoPlayer->
                        binding.videoView.player = exoPlayer
                    }

                val videoUrl = "https://appdata2.rendev.kr/2022/Yesan/Video/Outdoor/video_intro_outdoor.mp4"
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                simpleExoPlayer.setMediaItem(mediaItem)
                simpleExoPlayer.addListener(playbackStateListener())
                simpleExoPlayer.prepare()
                simpleExoPlayer.play()
            }
        }
    }

    private fun playbackStateListener() = object : Player.EventListener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
                else -> "UNKNOWN_STATE"
            }

            if (stateString == "ExoPlayer.STATE_ENDED") {
                val questMapActivity = activity as QuestMapActivity?
                questMapActivity?.onFragmentChange(AppConstants.TYPE_MINIMAP)
            }
        }
    }


    private fun clickEvent() {

        //game start
        binding.btnStart.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                val questMapActivity = activity as QuestMapActivity?
                questMapActivity?.onFragmentChange(AppConstants.TYPE_MINIMAP)
            }
        })

        //더는 보지 않기
        binding.btnIntroInvisible.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                activity?.let {activity ->
                    type?.let {
                        if (type == AppConstants.OUT_DOOR_TYPE) {
                            AppDataManager.getInstance(activity.application as AppApplication).setOutDoorIntroInvisible(true)
                            val questMapActivity = activity as QuestMapActivity?
                            questMapActivity?.onFragmentChange(AppConstants.TYPE_MINIMAP)

                        } else {
                            AppDataManager.getInstance(activity.application as AppApplication).setInDoorIntroInvisible(true)
                            val questMapActivity = activity as QuestMapActivity?
                            questMapActivity?.onFragmentChange(AppConstants.TYPE_MINIMAP)
                        }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.videoView.player?.release()
    }

}