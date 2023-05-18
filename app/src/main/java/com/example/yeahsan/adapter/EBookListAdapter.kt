package com.example.yeahsan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yeahsan.R
import com.example.yeahsan.data.api.model.CollectionContentVO
import okio.blackholeSink
import org.json.JSONArray

class EBookListAdapter(
    private var context: Context,
    private var list: ArrayList<CollectionContentVO>,
    private var onClickListener: View.OnClickListener
) : RecyclerView.Adapter<EBookListAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_artifact, parent, false)
        return Holder(view)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.name.text = list[position].title

        val imageResource = context.resources.getIdentifier("${list[position].url}_01","drawable",context.packageName)
        holder.image.setImageResource(imageResource)

        holder.image.tag = position
        holder.image.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image : ImageView = itemView.findViewById(R.id.iv_item)
        val name : TextView = itemView.findViewById(R.id.tv_name)
    }
}