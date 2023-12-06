package com.gaurav.newsapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.newsapp.adapter.NewsAdapter
import com.gaurav.newsapp.adapter.OnNewsClickListener
import com.gaurav.newsapp.common.BaseFragment
import com.gaurav.newsapp.databinding.FragmentHomeBinding
import com.gaurav.newsapp.model.Article
import com.gaurav.newsapp.ui.activity.ArticleActivity
import com.gaurav.newsapp.utils.AppHelper
import com.gaurav.newsapp.utils.launchActivity
import com.gaurav.newsapp.viewModel.NewsViewModel
import com.gaurav.newsapp.webservices.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),OnNewsClickListener {

    companion object{
        var category:String? = null
    }

    @Inject
    lateinit var nAdapter: NewsAdapter

    lateinit var viewModel: NewsViewModel

    private var newsList:List<Article>? = null

    override fun init() {
        super.init()
    }

    override fun setVariables() {
        super.setVariables()
        viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        newsList = listOf()
        nAdapter.bindListener(this)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.rvNewsList.layoutManager = LinearLayoutManager(mContext)
        binding.rvNewsList.adapter = nAdapter

    }

    override fun observeData() {
        super.observeData()
        viewModel.news.observe(this){
            hideLoading()
            newsList = null
            when(it){

                is NetworkResult.Success -> {
                    val article = it.data.articles
                    newsList = article
                    nAdapter.submitList(newsList)
                }
                is NetworkResult.Error -> {

                    val message = "${it.code} ${it.message}"
                    showErrorsMessage(message = message)
                }
                is NetworkResult.Exception -> {
                    val message = it.e.message
                    showErrorsMessage(message = message)
                }
                is NetworkResult.Loading -> {
                    showLoading()
                }

            }
        }
    }

    private fun showErrorsMessage(message: String?){

        binding.rvNewsList.visibility = View.GONE
        if (message!=null) {
            snackBarWithAction(binding.parentLayout, message) {
                if (category != null)
                    viewModel.getNews(category!!)
            }
        }
    }


    private fun hideLoading(){
        binding.apply {
            loading.visibility = View.GONE
            rvNewsList.visibility = View.VISIBLE
        }

    }

    private fun showLoading(){
        binding.apply {
            loading.visibility = View.VISIBLE
            rvNewsList.visibility = View.GONE
        }
    }

    override fun onNewsSelected(
        newsURl: String?,
        imageURL: String?,
        newsTitle: String?,
        newsTime: String?
    ) {
        if(newsURl != null){

            launchActivity<ArticleActivity> {
                val bundle = Bundle()
                bundle.putString(AppHelper.NEWS_URL, newsURl)
                bundle.putString(AppHelper.NEWS_TIME,newsTime)
                bundle.putString(AppHelper.IMAGE_URL,imageURL)
                bundle.putString(AppHelper.NEWS_TITLE,newsTitle)

                this.putExtra(AppHelper.NEWS_DATA,bundle)

            }
        }
    }
}