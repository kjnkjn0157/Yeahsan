package com.example.yeahsan.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yeahsan.R

import com.example.yeahsan.data.api.model.DoorListVO
import org.w3c.dom.Text

class MissionFragmentAdapter(private var context : Context, private var list : ArrayList<DoorListVO>, private var onClickListener: View.OnClickListener) : RecyclerView.Adapter<MissionFragmentAdapter.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionFragmentAdapter.Holder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_mission_progress_list, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: MissionFragmentAdapter.Holder, position: Int) {

        holder.title.text = list[position].name
        holder.btnClear.setTag(R.id.TAG_POSITION,position)
        holder.btnClear.setTag(R.id.TAG_IV,holder.image)
        holder.btnClear.setOnClickListener(onClickListener)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    class Holder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        val title : TextView = itemView.findViewById(R.id.tv_quest_title)
        val image : ImageView = itemView.findViewById(R.id.iv_item)
        val btnClear : TextView = itemView.findViewById(R.id.tv_quest_clear)
    }
}