package com.gaurav.newsapp.di

import androidx.room.Room
import com.gaurav.newsapp.NewsApplication
import com.gaurav.newsapp.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {


    @Provides
    fun provideDatabase():NewsDatabase{
        return Room.databaseBuilder(
            NewsApplication.instance,
            NewsDatabase::class.java,
            "news_db")
            .build()
    }

}