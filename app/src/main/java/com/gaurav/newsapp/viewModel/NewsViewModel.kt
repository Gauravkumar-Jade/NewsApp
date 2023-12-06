package com.gaurav.newsapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.newsapp.database.NewsData
import com.gaurav.newsapp.model.NewsModel
import com.gaurav.newsapp.repository.NewsRepository
import com.gaurav.newsapp.webservices.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository):ViewModel() {

    val news: LiveData<NetworkResult<NewsModel>> = repository.news
    val newsData: LiveData<List<NewsData>> = repository.newsData


    fun getNews(category: String){
        viewModelScope.launch {
            repository.getNewsData(category)
        }
    }


    fun insertNews(newsData: NewsData): Deferred<Long> {
        val result = viewModelScope.async {
            delay(500)
            repository.insertNews(newsData)
        }
        return result
    }

    fun getAllNews(){
        viewModelScope.launch {
            repository.getAllNews()
        }
    }

    fun deleteNews(newsData: NewsData){
        viewModelScope.launch {
            repository.deleteNews(newsData)
        }
    }

    fun deleteAllNews(){
        viewModelScope.launch {
            repository.deleteAllNews()
        }
    }

    fun isNewsExists(title:String):Deferred<Boolean>{
        val result = viewModelScope.async {
            delay(500)
            repository.isDataExits(title)
        }
        return result
    }

}