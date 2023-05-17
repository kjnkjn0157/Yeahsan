package com.example.yeahsan.ui.artifact

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yeahsan.adapter.EBookListAdapter

import com.example.yeahsan.databinding.FragmentEBookBinding
import org.json.JSONArray
import org.json.JSONObject


class EBookFragment : Fragment() {

    private lateinit var binding: FragmentEBookBinding
    private var jArray: JSONArray = JSONArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        initView()
    }

    private fun getData() {

        val assetManager = resources.assets
        val inputStream = assetManager.open("collectionList.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val jObject = JSONObject(jsonString)
        jArray = jObject.getJSONArray("collection")

        Log.e("TAG", "jObject ::: $jArray")
    }

    private fun initView() {

        binding.rvArtifact.adapter = context?.let {
            EBookListAdapter(it, jArray) {

            }
        }
    }

}