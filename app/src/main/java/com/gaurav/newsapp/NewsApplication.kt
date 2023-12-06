package com.gaurav.newsapp

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication: Application(){

    companion object{
        lateinit var instance: NewsApplication
        lateinit var API_KEY:String
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        API_KEY = ai.metaData.getString("apiKey").toString()
    }

}