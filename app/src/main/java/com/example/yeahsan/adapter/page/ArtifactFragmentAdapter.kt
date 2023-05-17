package com.example.yeahsan.adapter.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yeahsan.ui.artifact.ThreeDCollectionFragment
import com.example.yeahsan.ui.artifact.CollectionListFragment
import com.example.yeahsan.ui.artifact.EBookFragment

class ArtifactFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, pagerCount : Int ) : FragmentStateAdapter(fragmentManager,lifecycle){

    private val pagerCount = pagerCount

    enum class ArtifactPage { COLLECTION , COLLECTION_LIST, E_BOOK  }

    override fun getItemCount(): Int {
        return pagerCount
    }

    private val collectionFragment = ThreeDCollectionFragment()

    override fun createFragment(position: Int): Fragment {

        when (position) {
            ArtifactPage.COLLECTION.ordinal -> {
                return ThreeDCollectionFragment()
            }
            ArtifactPage.COLLECTION_LIST.ordinal -> {
                return CollectionListFragment()
            }
            ArtifactPage.E_BOOK.ordinal -> {
                return EBookFragment()
            }
        }
        return collectionFragment
    }
}