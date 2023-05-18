package com.example.yeahsan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.yeahsan.R

class EBookDetailAdapter(private var context : Context, private var list : ArrayList<String>) : RecyclerView.Adapter<EBookDetailAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_pager, parent , false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val image = context.resources.getIdentifier(list[position],"drawable",context.packageName)
        holder.image.setImageResource(image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image : ImageView = itemView.findViewById(R.id.iv_item)
    }
}