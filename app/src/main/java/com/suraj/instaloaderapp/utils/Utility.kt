package com.suraj.instaloaderapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.InstaLoaderApplication
import com.suraj.instaloaderapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        InstaLoaderApplication.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}



fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.isGone(): Boolean = visibility == View.GONE

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun loadImage(view: ImageView, url: String) {

}

fun Activity.showOnUiThread(init: Activity.() -> Unit): Activity {
    if (!isFinishing || !isDestroyed) {
        runOnUiThread {
            init()
        }
    }
    return this
}

fun Activity.snackbarMessage(message: String) {
    try {
        showOnUiThread {
            val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            val textView = snackBar.view.findViewById(R.id.snackbar_text) as TextView
            val robotoTypeface = ResourcesCompat.getFont(this, R.font.roboto_slab_regular)
            textView.typeface = robotoTypeface
            snackBar.show()
        }
    } catch (ex: Exception) {
    }
}
