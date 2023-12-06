package com.gaurav.newsapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Date

// Start new Activity functions

/*
* Start Activity from Activity
* */
inline fun <reified T : Any> Context.launchActivity(
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent)
}

/*
* Start Activity from Activity
* */
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (requestCode == -1) startActivity(intent)
    else startActivityForResult(intent, requestCode)
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    extras: Bundle.() -> Unit = {},
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.putExtras(Bundle().apply(extras))
    if (requestCode == -1) startActivity(intent)
    else startActivityForResult(intent, requestCode)
}


inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this.requireContext())
    intent.init()
    if (requestCode == -1)
        startActivity(intent)
    else
        startActivityForResult(intent, requestCode)

}

fun Activity.shareNews(newsURl:String){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject")
    intent.putExtra(Intent.EXTRA_TEXT, newsURl)
    startActivity(Intent.createChooser(intent,"Share News"))
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


/**
 * Extension Function for TextView to get simple date & time
 */
fun TextView.getSimpleDateTimeFormat(dateTime: String?){
    val inputDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val outputDateTime = SimpleDateFormat("dd-MM-yy',' hh:mm:a")
    val dateParse: Date = inputDateTime.parse(dateTime)
    this.text = outputDateTime.format(dateParse)
}
