package com.suraj.instaloader.repo

import android.graphics.Bitmap
import com.suraj.instaloader.bacgroundTasks.DownloadResourceFromRemote
import com.suraj.instaloader.cache.LRUCacheJson
import com.suraj.instaloader.cache.MemoryCache
import com.suraj.instaloader.core.ExecutorSupplier
import com.suraj.instaloader.interfaces.Caches
import com.suraj.instaloader.requestbuilder.InstaLoaderRequestBuilder
import com.suraj.instaloader.requestbuilder.ResponseType
import com.suraj.instaloader.utils.Constants
import com.suraj.instaloader.utils.Utils
import okhttp3.Response
import okio.Okio
import org.json.JSONArray
import java.lang.Exception
import java.util.HashMap
import java.util.concurrent.Future


/*Created by suraj on 24/02/2020*/


/*This is an repo class for handling the api call and data cache
*
*
*
*
* */
class InstaLoaderRepository(
    var memoryCache: MemoryCache,
    var newFixedThreadPool: ExecutorSupplier,
    var hashMap: HashMap<String, Future<Response?>>,
    var lruCacheJson: LRUCacheJson
) : Caches, Caches.CacheJson {
    var bitmapResponseHandler: (status:Boolean,bitmap: Bitmap?,message:String?) -> Unit = {status,bitmap, message ->  }
    var jsonResponseHandler: (status:Boolean,json: JSONArray?,message:String?) -> Unit = {status,bitmap, message ->  }


    override fun putJson(url: String, json: String) {
        lruCacheJson.put(url,json)
    }

    override fun getJson(url: String): String? {
        return lruCacheJson.get(url)
    }

    override fun clearJson() {
       lruCacheJson.clearCache()
    }


    override fun putImage(url: String, bitmap: Bitmap) {
        memoryCache.putImage(url, bitmap)
    }

    override fun getImage(url: String): Bitmap? {
        return memoryCache.getImage(url)
    }

    override fun evictAllBitmap() {
       memoryCache.evictAllBitmap()
    }


    /*
    * Load image from cache or network
    *@param requestBuilder
    *
    * */

    fun loadImage(requestBuilder: InstaLoaderRequestBuilder) {
        var bitmap = getImage(requestBuilder.url)
        bitmap?.let {
            bitmapResponseHandler(true,bitmap,null)
            return
        } ?: kotlin.run {
            var future = newFixedThreadPool.forBackgroundTasks().submit(DownloadResourceFromRemote(requestBuilder))
            hashMap.put(requestBuilder.url, future)
            newFixedThreadPool.forBackgroundTasks().execute {
                getStreamBasedOnResponse(future.get(),requestBuilder)
            }

        }
    }




    /*
    *
    * Load json from network or memory cache
    * @param InstaLoaderRequestBuilder
    * */
    fun loadJson(requestBuilder: InstaLoaderRequestBuilder){
        var jsonValue:String? = getJson(requestBuilder.url)
        jsonValue?.let {
            jsonResponseHandler(true,JSONArray(jsonValue),"")
        } ?: kotlin.run {
            var future=newFixedThreadPool.forBackgroundTasks().submit(DownloadResourceFromRemote(requestBuilder))
            hashMap.put(requestBuilder.url,future)
            newFixedThreadPool.forBackgroundTasks().execute {
                getStreamBasedOnResponse(future.get(),requestBuilder)
            }


        }
    }



    /*
    * Cancel all the network request
    *
    * */

    fun cancelAllRequest(){
        synchronized (this) {
            hashMap.forEach{
                if ( !it.value.isDone)
                    it.value.cancel(true)
            }
            hashMap.clear()
        }
    }


    /*
    * Cancel single network request
    *
    * */

    fun cancelSingleRequest(url: String){
        synchronized(this){
            hashMap.forEach {
                if (it.key == url &&  !it.value.isDone)
                    it.value.cancel(true)
            }
        }

    }


    /*
    *
    *Handling the response return by DownloadResourceFromRemote callable function
    *
    *
    *
    * */

    private fun getStreamBasedOnResponse(
        response: Response?,
        requestBuilder: InstaLoaderRequestBuilder) {

            response?.let {
                try {


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
                                    if (requestBuilder.enableCache) {
                                        putImage(requestBuilder.url, it)
                                    }

                                    bitmapResponseHandler(true, bitmap, null)
                                } ?: kotlin.run {
                                    bitmapResponseHandler(
                                        false,
                                        null,
                                        response.body().toString()
                                    )
                                }

                            }
                            ResponseType.JSONARRAY -> {
                                var jsonArray =
                                    JSONArray(Okio.buffer(response.body()!!.source()).readUtf8())
                                jsonArray?.let {
                                    putJson(requestBuilder.url, "" + it)
                                    jsonResponseHandler(true, it, null)
                                } ?: kotlin.run {
                                    jsonResponseHandler(false, null, response.body().toString())
                                }

                            }
                            else -> {
                            }
                        }
                    }
                } catch (e: Exception) {

                    handleError(e.message, requestBuilder)

                }
            } ?: kotlin.run {


                handleError(null, requestBuilder)

            }

        }




    /*
    *
    * method for handling the error
    *
    * */

    private fun handleError(errorMessage:String?,requestBuilder: InstaLoaderRequestBuilder) {

        var message:String=""
        errorMessage?.let {
            message=it
        }   ?: kotlin.run {
            message=Constants.errorString
        }
        when(requestBuilder.responseType){
            ResponseType.JSONARRAY->{

                jsonResponseHandler(false,null,message)
                }
            ResponseType.IMAGE->{
                    bitmapResponseHandler(false,null,message)
                }
            else->{ }

        }


}


}
