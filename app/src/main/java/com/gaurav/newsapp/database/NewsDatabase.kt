package com.gaurav.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewsData::class], version = 1, exportSchema = false)
abstract class NewsDatabase:RoomDatabase() {

    abstract fun getDao():NewsDao
}