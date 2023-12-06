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
import com.gaurav.newsapp.database.NewsData
import com.gaurav.newsapp.databinding.RawNewsListBinding
import com.gaurav.newsapp.utils.getSimpleDateTimeFormat
import javax.inject.Inject

class SavedNewsAdapter @Inject constructor():ListAdapter<NewsData,SavedNewsAdapter.SavedNewsHolder>(DiffUtilCall) {

    private var onSavedNewsClickListener:OnSavedNewsClickListener? = null

    fun bindListener(onSavedNewsClickListener: OnSavedNewsClickListener){
        this.onSavedNewsClickListener = onSavedNewsClickListener
    }


    inner class SavedNewsHolder(private val binding: RawNewsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(newsData:NewsData){
            binding.tvTitle.text = newsData.title
            binding.tvDate.getSimpleDateTimeFormat(newsData.time)

            Glide.with(NewsApplication.instance)
                .load(newsData.imageUrl)
                .override(Target.SIZE_ORIGINAL)
                .error(R.drawable.baseline_hide_image_24)
                .into(binding.ivImage)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsHolder {
       return SavedNewsHolder(RawNewsListBinding.inflate(
           LayoutInflater.from(parent.context), parent, false
       ))
    }

    override fun onBindViewHolder(holder: SavedNewsHolder, position: Int) {
        val data = getItem(position)
        holder.bindData(newsData = data)

        holder.itemView.setOnClickListener {
            onSavedNewsClickListener?.onNewsSelected(
                newsURl = data.url,
                newsTitle = data.title
            )
        }

        holder.itemView.setOnLongClickListener {
            onSavedNewsClickListener?.onDeleteSelectedNews(newsData = data, position = position)

            return@setOnLongClickListener false
        }
    }

    companion object DiffUtilCall:DiffUtil.ItemCallback<NewsData>(){
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem.url == newItem.url
        }

    }
}

interface OnSavedNewsClickListener{
    /**
     * Function for Deleting Selected News
     */
    fun onDeleteSelectedNews(newsData:NewsData?, position: Int)

    /**
     * Function for Viewing News Article
     */
    fun onNewsSelected(newsURl: String?, newsTitle:String?)
}