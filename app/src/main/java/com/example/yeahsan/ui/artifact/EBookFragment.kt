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
import com.example.yeahsan.adapter.EBookListAdapter
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.CollectionContentVO

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
    }

    private fun getData() {

        AppDataManager.getInstance(activity?.application as AppApplication).getEBookCollection {
            setAdapter(it)
        }
    }

    private fun setAdapter(list : ArrayList<CollectionContentVO>?) {

        binding.rvArtifact.adapter = context?.let {context ->
            list?.let { ebook ->
                EBookListAdapter(context, ebook) {
                    val position = it.tag as Int
                    val item = list[position]
                    val intent = Intent(context , EBookDetailActivity::class.java)
                    intent.putExtra(AppConstants.E_BOOK_ITEM, item)
                    Log.e("TAG","book item ::: $item")
                    startActivity(intent)
                }
            }
        }
    }

}