package com.gaurav.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gaurav.newsapp.database.NewsData
import com.gaurav.newsapp.database.NewsDatabase
import com.gaurav.newsapp.model.NewsModel
import com.gaurav.newsapp.webservices.APIService
import com.gaurav.newsapp.webservices.NetworkResult
import com.gaurav.newsapp.webservices.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val apiService: APIService,
    private val database: NewsDatabase) {

    private var _news = MutableLiveData<NetworkResult<NewsModel>>()
    val news:LiveData<NetworkResult<NewsModel>> get() = _news


    private var _newsData = MutableLiveData<List<NewsData>>()
    val newsData:LiveData<List<NewsData>> get() = _newsData


    suspend fun getNewsData(category: String){
        _news.postValue(NetworkResult.Loading())
        val news = safeApiCall(Dispatchers.IO){
           apiService.getNews(category)
        }

        _news.postValue(news)
    }


    suspend fun insertNews(newsData: NewsData):Long{
        val longValue:Long
        withContext(Dispatchers.IO){
           longValue =  database.getDao().insertNews(newsData)
        }
        return longValue
    }

    suspend fun getAllNews(){
        withContext(Dispatchers.IO){
            val news = database.getDao().getAllNews()
            _newsData.postValue(news)
        }
    }

    suspend fun deleteNews(newsData: NewsData){
        withContext(Dispatchers.IO){
            database.getDao().deleteNews(newsData)
        }

    }

    suspend fun deleteAllNews(){
        withContext(Dispatchers.IO){
            database.getDao().deleteAllNews()
        }
    }

    suspend fun isDataExits(title:String):Boolean{
        var isExists:Boolean
        withContext(Dispatchers.IO){
            isExists =  database.getDao().isDataExists(title)
        }
        return isExists
    }
}