package com.suraj.instaloader.repo

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
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

    private val lock = Any()


    override fun putJson(url: String, json: String) {
        lruCacheJson.put(url, json)
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

    fun loadImage(
        requestBuilder: InstaLoaderRequestBuilder,
        responseHandlerListener: (status: Boolean, data: Any?, message: String?) -> Unit
    ) {

            var bitmap = getImage(requestBuilder.url)
            bitmap?.let {
                responseHandlerListener.invoke(true, bitmap, "")
            } ?: kotlin.run {
                var future = newFixedThreadPool.forBackgroundTasks()
                    .submit(DownloadResourceFromRemote(requestBuilder))
                hashMap.put(requestBuilder.url, future)
                newFixedThreadPool.forBackgroundTasks().submit {
                    getStreamBasedOnResponse(future.get(), requestBuilder, responseHandlerListener)
                }


        }
    }


    /*
    *
    * Load json from network or memory cache
    * @param InstaLoaderRequestBuilder
    * */
    fun loadJson(
        requestBuilder: InstaLoaderRequestBuilder,
        responseHandlerListener: (status: Boolean, data: Any?, message: String?) -> Unit
    ) {
            var jsonValue: String? = getJson(requestBuilder.url)
            jsonValue?.let {
                responseHandlerListener(true, JSONArray(it), "")
            } ?: kotlin.run {
                var future = newFixedThreadPool.forBackgroundTasks()
                    .submit(DownloadResourceFromRemote(requestBuilder))
                hashMap.put(requestBuilder.url, future)
                newFixedThreadPool.forBackgroundTasks().execute  {

                    getStreamBasedOnResponse(future.get(), requestBuilder, responseHandlerListener)
                }


            }

    }


    /*
    * Cancel all the network request
    *
    * */

    fun cancelAllRequest() {
        synchronized(this) {
            hashMap.forEach {
                if (!it.value.isDone)
                    it.value.cancel(true)
            }
            hashMap.clear()
        }
    }


    /*
    * Cancel single network request
    *
    * */

    fun cancelSingleRequest(url: String) {
        synchronized(this) {
            hashMap.forEach {
                if (it.key == url && !it.value.isDone)
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
        requestBuilder: InstaLoaderRequestBuilder,
        responseHandlerListener: (status: Boolean, data: Any?, message: String?) -> Unit
    ) {

        response?.let {
            try {
                if (response.code() == 200) {
                    when (requestBuilder.responseType) {
                        ResponseType.IMAGE -> {
                            synchronized(lock) {
//                                var bitmap = Utils.decodeBitmap(
//                                    response,
//                                    requestBuilder.bitmapReqWidth,
//                                    requestBuilder.bitmapReqHeight,
//                                    requestBuilder.bitmapConfig,
//                                    requestBuilder.imageScaleType
//                                )
                                var bitmap =Utils.decodeResponse(response)
                            bitmap?.let {
                                if (requestBuilder.enableCache) {
                                    putImage(requestBuilder.url, it)
                                }
                                responseHandlerListener.invoke(true, bitmap, "")
                            }
                            } ?: kotlin.run {
                                responseHandlerListener(
                                    false,
                                    null,
                                    response.body().toString()
                                )
                            }

                        }
                        ResponseType.JSONARRAY -> {
                            var jsonArray: JSONArray? =
                                JSONArray(Okio.buffer(response.body()!!.source()).readUtf8())

                            jsonArray?.let {
                                putJson(requestBuilder.url, "" + it)
                                responseHandlerListener(true, it, null)
                            } ?: kotlin.run {
                                responseHandlerListener(false, null, response.body().toString())
                            }

                        }
                        else -> {
                        }
                    }
                }
            } catch (e: Exception) {

                handleError(e.message, requestBuilder, responseHandlerListener)

            }
        } ?: kotlin.run {


            handleError(null, requestBuilder, responseHandlerListener)

        }

    }


    /*
    *
    * method for handling the error
    *
    * */

    private fun handleError(
        errorMessage: String?,
        requestBuilder: InstaLoaderRequestBuilder,
        responseHandlerListener: (status: Boolean, data: Any?, message: String?) -> Unit
    ) {

        var message: String = ""
        errorMessage?.let {
            message = it
        } ?: kotlin.run {
            message = Constants.errorString
        }
        when (requestBuilder.responseType) {
            ResponseType.JSONARRAY -> {
                responseHandlerListener(false, null, message)
            }
            ResponseType.IMAGE -> {
                responseHandlerListener(false, null, message)
            }
            else -> {
            }

        }


    }


}
