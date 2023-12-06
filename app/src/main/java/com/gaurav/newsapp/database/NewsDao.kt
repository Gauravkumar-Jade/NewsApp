package com.gaurav.newsapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsData:NewsData):Long

    @Query("SELECT * FROM NewsData")
    suspend fun getAllNews(): List<NewsData>

    @Delete
    suspend fun deleteNews(newsData:NewsData)

    @Query("DELETE FROM NewsData")
    suspend fun deleteAllNews()

    @Query("SELECT EXISTS(SELECT * FROM NewsData WHERE title = :title)")
    suspend fun isDataExists(title:String):Boolean
}