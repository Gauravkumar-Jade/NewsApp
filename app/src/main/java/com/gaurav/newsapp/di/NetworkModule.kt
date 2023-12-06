package com.gaurav.newsapp.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.gaurav.newsapp.BuildConfig
import com.gaurav.newsapp.NewsApplication
import com.gaurav.newsapp.utils.AppHelper
import com.gaurav.newsapp.webservices.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideHttpClient() = if(BuildConfig.DEBUG){

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor{chain ->
            val urls = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("apiKey", NewsApplication.API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(urls)
                .build()

            return@Interceptor chain.proceed(request)
        }

        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(requestInterceptor)
            .addInterceptor(ChuckerInterceptor(NewsApplication.instance))
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient):Retrofit{
        return  Retrofit.Builder()
            .baseUrl(AppHelper.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAPIService(retrofit: Retrofit):APIService{
        return retrofit.create(APIService::class.java)
    }

}