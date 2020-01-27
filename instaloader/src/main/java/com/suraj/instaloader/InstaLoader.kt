package com.suraj.instaloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suraj.instaloader.cache.CachEvict
import com.suraj.instaloader.cache.Config
import com.suraj.instaloader.cache.LRUCacheJson
import com.suraj.instaloader.cache.MemoryCache
import com.suraj.instaloader.core.ExecutorSupplier
import com.suraj.instaloader.manager.PlaceHolder
import com.suraj.instaloader.manager.ResponseState
import com.suraj.instaloader.repo.InstaLoaderRepository
import com.suraj.instaloader.requestbuilder.InstaLoaderRequestBuilder
import com.suraj.instaloader.requestbuilder.MethodType
import com.suraj.instaloader.requestbuilder.ResponseType
import okhttp3.Response
import org.json.JSONArray
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.Future


/*Created by suraj on 24/02/2020
*
*
** InstaLoader entry point.
 * You must initialize this class before use. The simplest way is to just do
 * {#code InstaLoader.init(context)}.
* */

class InstaLoader : CachEvict {
    companion object {
        private var contextWeakReference: WeakReference<Context>? = null
        private lateinit var fastloader: InstaLoader
        lateinit var repository: InstaLoaderRepository
        var responseHandlerListener: (status: Boolean, data: Any?, message: String?) -> Unit =
            { status, data, message -> }

        fun with(context: Context): InstaLoader {
            contextWeakReference = WeakReference(context)
            fastloader = InstaLoader()

            return getInstance()
        }

        /**
         * Initializes InstaLoader with the default config.
         *
         * @param context The context
         */

        fun init(context: Context) {
            contextWeakReference = WeakReference(context)
            fastloader = InstaLoader()

            repository = this.getInstance().getRepositoryWithCache(Config.defaultCacheSize)
        }


        private fun getContext(): Context? {
            return contextWeakReference?.get()
        }

        /**
         * @return the InstaLoader instance
         */
        fun getInstance(): InstaLoader {
            return fastloader
        }
    }

    /*
       *
       * Instaloader default variables
       *
       *
       * */
    private var mUrl: String? = null
    private var enableCache = true
    private var mRes: Int = 0
    private var mPlaceHolderBitmap: Bitmap? = null
    private var mWidth = 0
    private var mHeight = 0
    private val handler = Handler(Looper.getMainLooper())


    /*
    * A Mutable live data for ResponseState
    *
    * */
    private var jsonResponseLiveData = MutableLiveData<ResponseState>()

    /*
    *
    * getJsonArrayResponse() is used to observe the ResponseState for caller method or function.It is invoked when user want json from an remote url
    * ie : {#code   InstaLoader.getInstance().source("JSON URL").loadJson()
            .getJsonArrayResponse().observe(this, Observer {
                when(it){
                    is ResponseState.loading -> { }
                    is ResponseState.Error->{ }
                    is ResponseState.Success->{}
                }
            })
            * }

    * */
    fun getJsonArrayResponse(): LiveData<ResponseState> = jsonResponseLiveData


    /*
    * @return the instance of InstaLoaderRepository with specified parameter,
    *
    *To use defined  cache size for  MemoryCache and LRUCacheJson simply call the getRepositoryWithCache(cacheSize)
    *
    * with required cache size
    *
    * */
    fun getRepositoryWithCache(capacity: Int): InstaLoaderRepository {
        return InstaLoaderRepository(
            MemoryCache(capacity)
            , ExecutorSupplier.getInstance(),
            HashMap<String, Future<Response?>>(),
            LRUCacheJson(capacity)
        )
    }


    /**
     * Image source by url
     * @param url
     */
    fun source(url: String): InstaLoader {
        mUrl = url
        mRes = -100
        return getInstance()
    }

    /**
     * Image source by drawable resource
     * @param res
     */
    fun source(@DrawableRes res: Int): InstaLoader {
        mRes = res
        mUrl = null
        return getInstance()
    }

    /**
     * Resize image
     * @param width
     * @param height
     */
    fun resize(width: Int, height: Int): InstaLoader {
        if (width != 0)
            mWidth = width
        if (height != 0)
            mHeight = height
        return getInstance()
    }

    /**
     * Placeholder image
     * @param placeholder - Drawable
     */
    fun placeholder(@AnyRes placeholder: Int): InstaLoader {

        if (placeholder != PlaceHolder.placeHolder) {
            try {
                // Try for drawable resource
                mPlaceHolderBitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                        getContext()?.resources,
                        placeholder
                    ), 300, 300, true
                )
                PlaceHolder.placeHolder = placeholder
                PlaceHolder.placeHolderBitmap = mPlaceHolderBitmap
                PlaceHolder.placeHolderColor = -1
            } catch (ignored: Exception) {
                PlaceHolder.placeHolder = placeholder
                PlaceHolder.placeHolderBitmap = null
                PlaceHolder.placeHolderColor = placeholder
            }

        } else {
            mPlaceHolderBitmap = PlaceHolder.placeHolderBitmap
        }
        return getInstance()
    }

    /**
     * Enable cache and set cache size - 0 to 1f
     * @param percent
     */
    fun enableCache(percent: Float): InstaLoader {
        enableCache = true
        return getInstance()
    }

    /**
     * Disable cache
     */
    fun disableCache(): InstaLoader {
        enableCache = false
        return getInstance()
    }

    /**
     * Load url or placeholder into @param ImageView
     * @param percent
     */

    fun into(imageView: ImageView) {

        mUrl?.let {
            if (it.startsWith("http") || it.startsWith("https")) {
                setDefaultIcons(imageView)
                imageView.tag = mUrl
                var requestBuilder = InstaLoaderRequestBuilder.Builder()
                    .setUrl(it)
                    .setMethodType(MethodType.GET)
                    .setResponseType(ResponseType.IMAGE)
                    .setScaleType(ImageView.ScaleType.FIT_XY)
                    .setBitmapWidth(if (mWidth == 0) imageView.width else mWidth)
                    .setBitmapHeight(if (mHeight == 0) imageView.height else mHeight)
                    .build()
                try {
                    repository.loadImage(
                        requestBuilder,
                        responseHandlerListener = { status, bitmap, message ->
                            bitmap?.let {
                                imageView.setImageBitmap(bitmap as Bitmap)
                            } ?: kotlin.run {
                                setDefaultIcons(imageView)
                            }
                        })
                } catch (e: Exception) {
                    setDefaultIcons(imageView)
                }

            } else {
                setDefaultIcons(imageView)
            }

        } ?: kotlin.run {
            setDefaultIcons(imageView)
        }

    }

    /*
    *
    * setting default place holder
    * @param ImageView
    *
    * */
    private fun setDefaultIcons(view: ImageView) {
        if (PlaceHolder.placeHolderBitmap != null) { // Placeholder is bitmap
            view.setImageBitmap(PlaceHolder.placeHolderBitmap)
        } else if (PlaceHolder.placeHolderColor != -1) { // Placeholder is color
            view.setImageResource(PlaceHolder.placeHolderColor)
        }
    }

    /*
    *
    * Setting the bitmap to ImageView from InstaLoaderRepository
    *Using higher order function in kotlin.
    *
    *
    * */


    /*
    *
    * Load Json from remote url
    *
    * */

    fun loadJson(): InstaLoader {
        mUrl?.let {
            jsonResponseLiveData.postValue(ResponseState.loading)
            var requestBuilder = InstaLoaderRequestBuilder.Builder()
                .setUrl(it)
                .setMethodType(MethodType.GET)
                .setResponseType(ResponseType.JSONARRAY)
                .build()

            try {
                repository.loadJson(
                    requestBuilder,
                    responseHandlerListener = { status, data, message ->
                        if (status) {
                            var json = data as JSONArray ?: null
                            json?.let { jsonResponseLiveData.postValue(ResponseState.Success(it)) }
                                ?: kotlin.run { jsonResponseLiveData.postValue(ResponseState.Error("" + message)) }
                        } else {
                            jsonResponseLiveData.postValue(ResponseState.Error("" + message))

                        }
                    }
                )
            } catch (e: Exception) {
                jsonResponseLiveData.postValue(
                    ResponseState.Error(
                        getContext()?.getString(R.string.invalid_URL) ?: ""
                    )
                )

            }
            R.string.app_name

        } ?: kotlin.run {
            jsonResponseLiveData.postValue(
                ResponseState.Error(
                    getContext()?.getString(R.string.invalid_URL) ?: ""
                )
            )

        }

        return getInstance()

    }


    /*
    * Evict all bitmap from memory cache
    * */

    override fun evictAllBitmap()  {
        repository.evictAllBitmap()

    }


    /*
    * Clear all json from memory
    * */
    override fun evictAllJson() {
        repository.clearJson()
    }


    /*
    * Cancel single request of InstaLoader
    * @param url
    * */
    override fun cancelSingleRequest(url: String) {
        repository.cancelSingleRequest(url)
    }

    /*
    *
    * Cancel all task of InstaLoader
    *
    * */

    override fun cancelAllTask() {
        repository.cancelAllRequest()

    }


    /*
    * Shutdown all task
    *
    * */
    fun shutDown(){
        evictAllBitmap()
        evictAllJson()
        cancelAllTask()

    }




}

