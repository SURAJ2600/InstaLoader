package com.suraj.instaloader.bacgroundTasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.suraj.instaloader.repo.InstaLoaderRepository
import com.suraj.instaloader.requestbuilder.InstaLoaderRequestBuilder
import com.suraj.instaloader.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class DownloadBitmapFromRemote (private val requestBuilder: InstaLoaderRequestBuilder
) : DownloadTask<Response?>()
{
    var client =  OkHttpClient();
    var request : Request?=null

    override fun download(url: String): Response? {
       var response:Response?=null
        try {
            // code request code here
            request =  Request.Builder()
                ?.url(url)
                ?.build();
            response = client.newCall(request).execute();

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


    override fun call(): Response? {
        val response = download(requestBuilder.url)
        return response
    }




}

