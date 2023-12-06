package com.gaurav.newsapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.view.View.OnClickListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.gaurav.newsapp.R
import com.gaurav.newsapp.common.BaseActivity
import com.gaurav.newsapp.database.NewsData
import com.gaurav.newsapp.databinding.ActivityArticleBinding
import com.gaurav.newsapp.utils.AppHelper
import com.gaurav.newsapp.utils.shareNews
import com.gaurav.newsapp.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : BaseActivity<ActivityArticleBinding>(ActivityArticleBinding::inflate), OnClickListener {

    private var newsUrl: String? =  null
    private var imageUrl: String? =  null
    private var newsTitle: String? =  null
    private var newsTime: String? =  null

    lateinit var viewModel:NewsViewModel
    override fun init() {
        super.init()

        val bundle = intent.getBundleExtra(AppHelper.NEWS_DATA)
        newsUrl = bundle?.getString(AppHelper.NEWS_URL)
        imageUrl = bundle?.getString(AppHelper.IMAGE_URL)
        newsTitle = bundle?.getString(AppHelper.NEWS_TITLE)
        newsTime = bundle?.getString(AppHelper.NEWS_TIME)
    }

    override fun setVariables() {
        super.setVariables()
        binding.listener = this
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setUpViews() {
        super.setUpViews()
        binding.apply {
            webView.loadUrl(newsUrl!!)
            webView.settings.javaScriptEnabled = true
            webView.settings.loadsImagesAutomatically = true
            webView.webViewClient = WebClient()
        }
    }

    override fun observeData() {
        super.observeData()

        isNewsDataExists()
    }

    private inner class WebClient: WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideLoading()
        }
    }

    override fun onClick(v: View?) {
       when(v){
           binding.btnSave -> {
               saveNewsData()
           }
           binding.btnShare -> {
               if(newsUrl != null){
                   shareNews(newsURl = newsUrl!!)
               }
           }
       }
    }

    private fun saveNewsData() {
        val newsData = NewsData(
            title = newsTitle,
            time = newsTime,
            url = newsUrl,
            imageUrl = imageUrl)

        val longValue = viewModel.insertNews(newsData)

        longValue.invokeOnCompletion {
            if(it == null){
                val result = longValue.getCompleted()
                if(result > 0L){
                    showSnackbar(binding.parentLayout,"News Saved!")
                    setSaveButton(
                        clickable = false,
                        imageDrawable = R.drawable.baseline_added_bookmark )
                }else{
                    showSnackbar(binding.parentLayout,"Unable To Save!")
                }
            }
        }
    }

    private fun isNewsDataExists() {
        if(newsTitle != null){
            val booleanValue = viewModel.isNewsExists(title = newsTitle!!)

            booleanValue.invokeOnCompletion {
                if(it == null){
                    val result = booleanValue.getCompleted()

                    if(result==true){
                        setSaveButton(
                            clickable = false,
                            imageDrawable = R.drawable.baseline_added_bookmark )
                    }else{
                        setSaveButton(
                            clickable = true,
                            imageDrawable = R.drawable.baseline_no_bookmark )
                    }
                }
            }
        }
    }


    private fun setSaveButton(clickable:Boolean, imageDrawable:Int){
        binding.btnSave.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,imageDrawable, null))
        binding.btnSave.isClickable = clickable
    }
}