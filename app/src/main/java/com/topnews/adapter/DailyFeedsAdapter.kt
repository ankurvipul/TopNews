package com.topnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topnews.R
import com.topnews.model.dailyfeedsresponse.Article
import com.topnews.util.AppUtils.convertDate
import com.topnews.util.AppUtils.isNullOrEmpty
import com.topnews.util.AppUtils.loadImage
import kotlinx.android.synthetic.main.item_news_feed.view.*
import org.koin.core.KoinComponent

class DailyFeedsAdapter(private val context: Context?, private var articles: ArrayList<Article>) :
    RecyclerView.Adapter<DailyFeedsAdapter.MyViewHolder>(), KoinComponent {

    var onItemClick: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_news_feed,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(articles[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(article: Article) {
            itemView.tv_news_title.text = article.title
            itemView.tv_news_published.text = convertDate(article.publishedAt)
            if (adapterPosition + 1 == articles.size)
                itemView.divider.visibility = View.GONE
            if (!isNullOrEmpty(article.urlToImage))
                loadImage(context, article.urlToImage, itemView.iv_news)
            itemView.setOnClickListener {
                onItemClick?.invoke(articles[adapterPosition])
            }
        }
    }
}