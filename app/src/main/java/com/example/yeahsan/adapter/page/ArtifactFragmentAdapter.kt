package com.example.yeahsan.adapter.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yeahsan.ui.artifact.CollectionFragment
import com.example.yeahsan.ui.artifact.EBookFragment

class ArtifactFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, pagerCount : Int ) : FragmentStateAdapter(fragmentManager,lifecycle){

    private val pagerCount = pagerCount

    enum class ArtifactPage { COLLECTION , E_BOOK  }

    override fun getItemCount(): Int {
        return pagerCount
    }

    private val collectionFragment = CollectionFragment()

    override fun createFragment(position: Int): Fragment {

        when (position) {
            ArtifactFragmentAdapter.ArtifactPage.COLLECTION.ordinal -> {
                return CollectionFragment()
            }
            ArtifactFragmentAdapter.ArtifactPage.E_BOOK.ordinal -> {
                return EBookFragment()
            }

        }
        return collectionFragment
    }
}