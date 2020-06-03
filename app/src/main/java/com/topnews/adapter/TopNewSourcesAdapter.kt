package com.topnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topnews.R
import com.topnews.model.topnewsourceresponse.Source
import kotlinx.android.synthetic.main.item_top_new.view.*

class TopNewSourcesAdapter(private val context: Context?, private var topNewSources: ArrayList<Source>) :
    RecyclerView.Adapter<TopNewSourcesAdapter.MyViewHolder>() {

    var onItemClick: ((Source) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_top_new, parent, false))
    }

    override fun getItemCount(): Int {
      return topNewSources.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(topNewSources[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(topNewSource: Source) {
            itemView.tv_source_name.text = topNewSource.name
            itemView.tv_source_description.text = topNewSource.description
            itemView.setOnClickListener {
                onItemClick?.invoke(topNewSources[adapterPosition])
            }
        }
    }
}