package com.example.yeahsan.ui.artifact

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.adapter.ArtifactListAdapter
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.databinding.FragmentCollectionBinding
import com.example.yeahsan.ui.vr.WebViewActivity

class ThreeDCollectionFragment : Fragment() {

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

        AppDataManager.getInstance(activity?.application as AppApplication).getThreeDCollection {
            setAdapter(it)
        }
    }

    private fun setAdapter(list: ArrayList<CollectionContentVO>?) {

        list?.let {
            binding.rvArtifact.adapter = context?.let { context ->
                ArtifactListAdapter(context, list) {
                    val position = it.tag as Int
                    val url = list[position].url

                    val intent = Intent(context,WebViewActivity::class.java)
                    intent.putExtra(AppConstants.STRING_TYPE,AppConstants.ARTIFACT_STRING)
                    intent.putExtra(AppConstants.URL_STRING,url)
                    startActivity(intent)
                }
            }
        }
    }
}