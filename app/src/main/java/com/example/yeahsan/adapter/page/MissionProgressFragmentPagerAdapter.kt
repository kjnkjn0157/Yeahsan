package com.example.yeahsan.adapter.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yeahsan.AppConstants
import com.example.yeahsan.ui.questionprogress.InsideMissionFragment
import com.example.yeahsan.ui.questionprogress.MissionListFragment

class MissionProgressFragmentPagerAdapter(fragmentManager:FragmentManager,lifecycle:Lifecycle,pagerCount : Int ) :FragmentStateAdapter(fragmentManager,lifecycle){

    private val pagerCount = pagerCount

    override fun getItemCount(): Int {
        return pagerCount
    }

    enum class QuestProgressFragment { OUTSIDE, INSIDE }

    private val outsideMissionListFragment = MissionListFragment()

    override fun createFragment(position: Int): Fragment {

        when (position) {
            QuestProgressFragment.OUTSIDE.ordinal -> {
                val outsideMissionListFragment = MissionListFragment()
                val bundle = Bundle()
                bundle.putString(AppConstants.STRING_TYPE,AppConstants.OUT_DOOR_TYPE)
                outsideMissionListFragment.arguments = bundle
                return outsideMissionListFragment
            }
            QuestProgressFragment.INSIDE.ordinal -> {
                val insideMissionFragment = MissionListFragment()
                val bundle = Bundle()
                bundle.putString(AppConstants.STRING_TYPE,AppConstants.IN_DOOR_TYPE)
                insideMissionFragment.arguments = bundle
                return insideMissionFragment
            }

        }
        return outsideMissionListFragment
    }
}