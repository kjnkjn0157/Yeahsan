package com.example.yeahsan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeahsan.R
import com.example.yeahsan.data.api.model.CollectionContentVO

class ArtifactListAdapter(
    private var context: Context,
    private var list: ArrayList<CollectionContentVO>,
    private var onClickListener: View.OnClickListener
) : RecyclerView.Adapter<ArtifactListAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_artifact, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder:Holder, position: Int) {

        holder.name.text = list[position].title

        val url = ""

        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.img_holder)
            .into(holder.image)

        holder.image.tag = position
        holder.image.setOnClickListener(onClickListener)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image : ImageView = itemView.findViewById(R.id.iv_item)
        var name : TextView = itemView.findViewById(R.id.tv_name)
    }
}