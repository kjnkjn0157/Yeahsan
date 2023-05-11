package com.example.yeahsan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeahsan.R

import com.example.yeahsan.data.api.model.DoorListVO

class MissionFragmentAdapter(
    private var context: Context,
    private var originList: ArrayList<DoorListVO>?,
    private var clearList: ArrayList<DoorListVO>?,
    private var onClickListener: View.OnClickListener?
) : RecyclerView.Adapter<MissionFragmentAdapter.Holder>() {

//    @SuppressLint("NotifyDataSetChanged")
//    fun setParams(_context: Context,
//                  _originList: ArrayList<DoorListVO>?,
//                  _clearList: ArrayList<DoorListVO>?,
//                  onClickListener: View.OnClickListener?) {
//
//        this.context = context
//        this.originList?.clear()
//        if (_originList != null) {
//            this.originList?.addAll(_originList)
//        }
//        this.clearList?.clear()
//        if (_clearList != null) {
//            this.clearList?.addAll(_clearList)
//        }
//        this.onClickListener = onClickListener
//        this.notifyDataSetChanged()
//        Log.e("TAG","dataset:::clear list  ${clearList?.size}")
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionFragmentAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mission_progress_list, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MissionFragmentAdapter.Holder, position: Int) {
        if (clearList != null && originList != null) {
            Log.e("TAG","adapter ::: ")
            for (i in 0 until clearList!!.size) {
                if (originList!![position].seq == clearList!![i].seq) {
                    holder.btnClear.text ="퀘스트 완료"
                    holder.btnClear.setBackgroundResource(R.mipmap.quest_progress_back_success)
                    holder.count.setBackgroundResource(R.mipmap.progress_count_success)
                    holder.image.setImageResource(R.drawable.quest_progress_success_ico)
                    holder.btnClear.setTag(R.id.TAG_POSITION, position)
                    holder.btnClear.setTag(R.id.TAG_IV, holder.image)
                    holder.btnClear.setOnClickListener(onClickListener)
                }
            }
            holder.title.text = originList!![position].name
            holder.count.text = (position + 1).toString()
            holder.btnClear.setTag(R.id.TAG_POSITION, position)
            holder.btnClear.setTag(R.id.TAG_IV, holder.image)
            holder.btnClear.setOnClickListener(onClickListener)
        } else {
            Log.e("TAG","adapter ::: else")
            holder.count.text = (position + 1).toString()
            holder.title.text = originList?.get(position)?.name ?: ""
            holder.btnClear.setTag(R.id.TAG_POSITION, position)
            holder.btnClear.setTag(R.id.TAG_IV, holder.image)
            holder.btnClear.setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int {

        return originList?.size ?: 0
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.tv_quest_title)
        val image: ImageView = itemView.findViewById(R.id.iv_item)
        val btnClear: TextView = itemView.findViewById(R.id.tv_quest_clear)
        val count: TextView = itemView.findViewById(R.id.tv_count)

    }
}