package com.example.yeahsan.ui.artifact

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.adapter.ArtifactListAdapter
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.databinding.FragmentCollectionListBinding
import com.example.yeahsan.ui.vr.WebViewActivity
import org.json.JSONArray

class CollectionListFragment : Fragment() {

    private lateinit var binding: FragmentCollectionListBinding
    private var jArray: JSONArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCollectionListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

    }

    private fun getData() {

        AppDataManager.getInstance(activity?.application as AppApplication).getBasicCollection {
            setAdapter(it)
        }
    }

    private fun setAdapter(list: ArrayList<CollectionContentVO>?) {

        list?.let {
            binding.rvArtifact.adapter = context?.let { context ->
                ArtifactListAdapter(context ,list) {
                    val position = it.tag as Int
                    val content = list[position]
                    val imageUrl = AppDataManager.getInstance(context.applicationContext as AppApplication).getFilePath() + list[position].path
                    Log.e("TAG","imageUrl ::: $imageUrl")
//                    val intent = Intent(context,WebViewActivity::class.java)
//                    intent.putExtra(AppConstants.EXTRA_ITEM,content)
//                    startActivity(intent)
                }
            }
        }
    }
}