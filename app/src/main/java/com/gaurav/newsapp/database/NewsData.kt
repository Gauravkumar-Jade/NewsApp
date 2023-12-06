package com.gaurav.newsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsData(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val  title: String?,
    val time: String?,
    val url: String?,
    val imageUrl: String?
)
