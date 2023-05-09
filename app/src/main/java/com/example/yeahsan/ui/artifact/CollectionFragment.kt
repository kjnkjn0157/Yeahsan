package com.example.yeahsan.ui.artifact

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.adapter.ArtifactListAdapter
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.FragmentCollectionBinding
import com.example.yeahsan.ui.vr.WebViewActivity

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
    }


    private fun getData() {

//        activity?.let { activity ->
//            val filePath = AppDataManager.getInstance(activity.application as AppApplication).getFilePath().toString()
//            AppDataManager.getInstance(activity.application as AppApplication)
//                .getCollectionListData {
//                    it?.collectionBodyVO?.collectionList.let { list ->
//                        context?.let { context ->
//                            binding.rvArtifact.adapter =
//                                list?.let { array ->
//                                    ArtifactListAdapter(context, array, filePath) {
//
//                                        val position = it.tag as Int
//                                        val url =
//                                            (AppDataManager.getInstance(activity.application as AppApplication)
//                                                .getFilePath()) + list[position].url
//                                        Log.e("TAG", "url ::: $url")
//
//                                        val intent = Intent(activity, WebViewActivity::class.java)
//                                        intent.putExtra(AppConstants.STRING_TYPE, AppConstants.ARTIFACT_STRING)
//                                        intent.putExtra(AppConstants.URL_STRING, url)
//                                        startActivity(intent)
//                                    }
//                                }
//                        }
//                    }
//                }
//        }
    }
}