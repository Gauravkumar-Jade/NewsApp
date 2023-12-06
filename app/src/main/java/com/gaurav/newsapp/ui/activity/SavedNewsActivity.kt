package com.gaurav.newsapp.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.newsapp.adapter.OnSavedNewsClickListener
import com.gaurav.newsapp.adapter.SavedNewsAdapter
import com.gaurav.newsapp.common.BaseActivity
import com.gaurav.newsapp.database.NewsData
import com.gaurav.newsapp.databinding.ActivitySavedNewsBinding
import com.gaurav.newsapp.utils.AppHelper
import com.gaurav.newsapp.utils.launchActivity
import com.gaurav.newsapp.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedNewsActivity : BaseActivity<ActivitySavedNewsBinding>(ActivitySavedNewsBinding::inflate),
    OnSavedNewsClickListener, OnClickListener {

    @Inject
    lateinit var savedNewsAdapter: SavedNewsAdapter

    lateinit var viewModel: NewsViewModel

    override fun init() {
        super.init()
    }

    override fun setVariables() {
        super.setVariables()
        savedNewsAdapter.bindListener(this)
        binding.listener = this
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.rvNewsList.layoutManager = LinearLayoutManager(this)
        binding.rvNewsList.adapter = savedNewsAdapter
    }

    override fun observeData() {
        super.observeData()
        loadSavedNewsFromDatabase()
    }

    private fun loadSavedNewsFromDatabase() {
        viewModel.getAllNews()
        viewModel.newsData.observe(this){
            if(it.isNotEmpty()){
                savedNewsAdapter.submitList(it)
            }else{
                showSnackbar(binding.parentLayout, "No Data!")
                binding.rvNewsList.visibility = View.GONE
            }
        }
    }

    override fun onDeleteSelectedNews(newsData: NewsData?, position: Int) {
        if(newsData != null){
            showDeleteAlertDialog("Delete Selected News?") {
                viewModel.deleteNews(newsData = newsData)
                loadSavedNewsFromDatabase()
            }
        }
    }

    override fun onNewsSelected(newsURl: String?, newsTitle: String?) {
        launchActivity<ArticleActivity> {
            val bundle = Bundle()
            bundle.putString(AppHelper.NEWS_URL, newsURl)
            bundle.putString(AppHelper.NEWS_TITLE,newsTitle)
            this.putExtra(AppHelper.NEWS_DATA,bundle)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnDeleteAll -> {
                showDeleteAlertDialog("Delete All News?") {
                    viewModel.deleteAllNews()
                    loadSavedNewsFromDatabase()
                }
            }
        }
    }

}