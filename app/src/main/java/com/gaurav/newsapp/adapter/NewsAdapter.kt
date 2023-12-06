package com.gaurav.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.gaurav.newsapp.NewsApplication
import com.gaurav.newsapp.R
import com.gaurav.newsapp.databinding.RawNewsListBinding
import com.gaurav.newsapp.model.Article
import com.gaurav.newsapp.utils.getSimpleDateTimeFormat
import javax.inject.Inject

class NewsAdapter @Inject constructor():ListAdapter<Article,NewsAdapter.NewsHolder>(DiffUtilCall) {


    private var onNewsClickListener:OnNewsClickListener? = null

    fun bindListener(onNewsClickListener:OnNewsClickListener?){
        this.onNewsClickListener = onNewsClickListener
    }


    inner class NewsHolder(private val binding:RawNewsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(article: Article){
            binding.tvTitle.text = article.title
            binding.tvDate.getSimpleDateTimeFormat(article.publishedAt)

            Glide.with(NewsApplication.instance)
                .load(article.urlToImage)
                .override(Target.SIZE_ORIGINAL)
                .error(R.drawable.baseline_hide_image_24)
                .into(binding.ivImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder(RawNewsListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val newsData = getItem(position)
        holder.bindData(newsData)

        holder.itemView.setOnClickListener{
            onNewsClickListener?.onNewsSelected(newsData.url, newsData.urlToImage, newsData.title, newsData.publishedAt)
        }
    }

    companion object DiffUtilCall: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.content == newItem.content
        }

    }
}

interface OnNewsClickListener{
    /**
     * Event Listener For NewsAdapter
     */
    fun onNewsSelected(newsURl: String?, imageURL:String?, newsTitle:String?, newsTime:String?)
}