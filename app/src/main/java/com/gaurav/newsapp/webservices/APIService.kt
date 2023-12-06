package com.gaurav.newsapp.webservices

import com.gaurav.newsapp.model.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("/v2/top-headlines?country=in")
    suspend fun getNews(@Query("category") category:String):Response<NewsModel>
}