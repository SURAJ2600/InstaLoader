package com.suraj.instaloader.repo

import android.graphics.Bitmap
import android.util.Log
import com.suraj.instaloader.bacgroundTasks.DownloadBitmapFromRemote
import com.suraj.instaloader.cache.LRUCacheJson
import com.suraj.instaloader.cache.MemoryCache
import com.suraj.instaloader.interfaces.Caches
import com.suraj.instaloader.requestbuilder.InstaLoaderRequestBuilder
import com.suraj.instaloader.requestbuilder.ResponseType
import com.suraj.instaloader.utils.Utils
import okhttp3.Response
import okio.Okio
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future


/*Created by suraj on 24/02/2020*/


/*This is an repo class for returing the bitmap or json from Remote or memory cache*/
class InstaLoaderRepository(
    var memoryCache: MemoryCache,
    var newFixedThreadPool: ExecutorService,
    var hashMap: HashMap<String, Future<Response?>>,
    var lruCacheJson: LRUCacheJson
) : Caches, Caches.CacheJson {
    var bitmapResponseHandler: (status:Boolean,bitmap: Bitmap?,message:String?) -> Unit = {status,bitmap, message ->  }
    var jsonResponseHandler: (status:Boolean,json: JSONArray?,message:String?) -> Unit = {status,bitmap, message ->  }


    override fun putJson(url: String, json: JSONObject) {
    }

    override fun getJson(url: String): JSONObject? {
        return null
    }

    override fun clearJson() {
    }


    override fun putImage(url: String, bitmap: Bitmap) {
        memoryCache.putImage(url, bitmap)
    }

    override fun getImage(url: String): Bitmap? {
        return memoryCache.getImage(url)
    }

    override fun clearImage() {
        memoryCache.clearImage()
    }

    fun loadImage(requestBuilder: InstaLoaderRequestBuilder) {
        var bitmap = memoryCache.getImage(requestBuilder.url)
        bitmap?.let {
            bitmapResponseHandler(true,bitmap,null)
            return
        } ?: kotlin.run {
            var future = newFixedThreadPool.submit(DownloadBitmapFromRemote(requestBuilder))
            hashMap.put(requestBuilder.url, future)
            getStreamBasedOnResponse(future.get(),requestBuilder)

        }
    }


    fun loadJson(requestBuilder: InstaLoaderRequestBuilder){
        var jsonValue:String? = lruCacheJson.get(requestBuilder.url)
        jsonValue?.let {
            jsonResponseHandler(true,JSONArray(jsonValue),"")
        } ?: kotlin.run {
            var future=newFixedThreadPool.submit(DownloadBitmapFromRemote(requestBuilder))
            hashMap.put(requestBuilder.url,future)
            getStreamBasedOnResponse(future.get(),requestBuilder)


        }



    }


    fun cancelAllRequest(){
        synchronized (this) {
            hashMap.forEach{
                if ( !it.value.isDone)
                    it.value.cancel(true)
            }
            hashMap.clear()
        }
    }

    fun cancelSingleTask(url: String){
        synchronized(this){
            hashMap.forEach {
                if (it.key == url &&  !it.value.isDone)
                    it.value.cancel(true)
            }
        }

    }


    /*
    *
    *
    *
    *
    *
    * */

    private fun getStreamBasedOnResponse(
        response: Response?,
        requestBuilder: InstaLoaderRequestBuilder) {

        response?.let {
            if (response.code() == 200) {
                when (requestBuilder.responseType) {
                    ResponseType.IMAGE -> {
                        var bitmap = Utils.decodeBitmap(
                            response,
                            requestBuilder.bitmapReqWidth,
                            requestBuilder.bitmapReqHeight,
                            requestBuilder.bitmapConfig,
                            requestBuilder.bitmapOptions,
                            requestBuilder.imageScaleType
                        )
                        bitmap?.let {
                            if(requestBuilder.enableCache){
                                memoryCache.putImage(requestBuilder.url, it)
                            }
                            bitmapResponseHandler(true,bitmap,null)
                        } ?: kotlin.run { bitmapResponseHandler(false,null,response.body().toString()) }

                    }
                    ResponseType.JSONARRAY -> {
                        var jsonArray= JSONArray(Okio.buffer(response.body()!!.source()).readUtf8())
                        jsonArray?.let {
                            lruCacheJson?.put(requestBuilder.url, ""+it)
                            jsonResponseHandler(true,it,null)
                        } ?: kotlin.run {
                            jsonResponseHandler(false,null, response.body().toString())
                        }
                    }
                    else -> {
                    }
                }
            }
        } ?: kotlin.run {

            when(requestBuilder.responseType){
                ResponseType.JSONARRAY->{ jsonResponseHandler(false,null, "Error accured please try latter".toString()) }
                ResponseType.IMAGE->{ bitmapResponseHandler(false,null,"Error accured please try latter".toString()) }
                else->{ }


            }

        }


    }


}
