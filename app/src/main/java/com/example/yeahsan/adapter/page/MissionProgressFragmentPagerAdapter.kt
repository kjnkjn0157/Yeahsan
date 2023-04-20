package com.example.yeahsan.adapter.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yeahsan.ui.questionprogress.InsideMissionFragment
import com.example.yeahsan.ui.questionprogress.OutsideMissionFragment

class MissionProgressFragmentPagerAdapter(fragmentManager:FragmentManager,lifecycle:Lifecycle,pagerCount : Int ) :FragmentStateAdapter(fragmentManager,lifecycle){

    private val pagerCount = pagerCount

    override fun getItemCount(): Int {
        return pagerCount
    }

    enum class QuestProgressFragment { OUTSIDE, INSIDE }

    val outsideMissionFragment = OutsideMissionFragment()

    override fun createFragment(position: Int): Fragment {

        when (position) {
            QuestProgressFragment.OUTSIDE.ordinal -> {
                val outsideMissionFragment = OutsideMissionFragment()
                return outsideMissionFragment
            }
            QuestProgressFragment.INSIDE.ordinal -> {
                val insideMissionFragment = InsideMissionFragment()
                return insideMissionFragment
            }

        }
        return outsideMissionFragment
    }
}