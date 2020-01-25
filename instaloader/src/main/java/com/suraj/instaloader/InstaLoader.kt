package com.suraj.instaloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
import java.util.concurrent.Executors
import java.util.concurrent.Future

class InstaLoader : CachEvict {


    private var mUrl: String? = null
    private var enableCache = true
    private var mRes: Int = 0
    private var mPlaceHolderBitmap: Bitmap? = null
    private var mWidth = 0
    private var mHeight = 0
    private var cacheAllowed = 1f
    private var jsonResponseLiveData = MutableLiveData<ResponseState>()


    companion object {
        private var contextWeakReference: WeakReference<Context>? = null
        private lateinit var fastloaderWeakReference: WeakReference<InstaLoader>
        lateinit var repository: InstaLoaderRepository

        fun with(context: Context): InstaLoader {
            contextWeakReference = WeakReference(context)
            val fastloader = InstaLoader()
            fastloaderWeakReference = WeakReference(fastloader)
            return getInstaLoader()
        }

        fun init(context: Context) {
            contextWeakReference = WeakReference(context)
            val fastloader = InstaLoader()
            fastloaderWeakReference = WeakReference(fastloader)
            repository = this.getInstaLoader().getRepositoryWithCache(Config.defaultCacheSize)
        }


        private fun getContext(): Context? {
            return contextWeakReference?.get()
        }

        fun getInstaLoader(): InstaLoader {
            return fastloaderWeakReference.get()!!
        }
    }

    fun getRepositoryWithCache(capacity: Int): InstaLoaderRepository {

        return InstaLoaderRepository(
            MemoryCache(capacity)
            , ExecutorSupplier.getInstance(),
            HashMap<String, Future<Response?>>(),
            LRUCacheJson(capacity)
        )
    }

    fun getJsonArrayResponse(): LiveData<ResponseState> = jsonResponseLiveData

    /**
     * Image source by url
     * @param url
     */
    fun source(url: String): InstaLoader {
        mUrl = url
        mRes = -100
        return getInstaLoader()
    }

    /**
     * Image source by drawable resource
     * @param res
     */
    fun source(@DrawableRes res: Int): InstaLoader {
        mRes = res
        mUrl = null
        return getInstaLoader()
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
        return getInstaLoader()
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
        return getInstaLoader()
    }

    /**
     * Enable cache and set cache size - 0 to 1f
     * @param percent
     */
    fun enableCache(percent: Float): InstaLoader {
        enableCache = true
        cacheAllowed = percent
        return getInstaLoader()
    }

    /**
     * Disable cache
     */
    fun disableCache(): InstaLoader {
        enableCache = false
        cacheAllowed = 0f
        return getInstaLoader()
    }

    fun into(imageView: ImageView) {
        mUrl?.let {
            setDefaultIcons(imageView)
            setBitmapHandler(imageView)
            var requestBuilder = InstaLoaderRequestBuilder.Builder()
                .setUrl(it)
                .setMethodType(MethodType.GET)
                .setResponseType(ResponseType.IMAGE)
                .setBitmapWidth(if (mWidth == 0) imageView.width else mWidth)
                .setBitmapHeight(if (mHeight == 0) imageView.height else mHeight)
                .build()
            repository.loadImage(requestBuilder)

        } ?: kotlin.run {
            setDefaultIcons(imageView)
        }

    }

    private fun setBitmapHandler(imageView: ImageView) {
        repository.bitmapResponseHandler = { status, bitmap, message ->
            bitmap?.let {
                imageView.setImageBitmap(bitmap)

            } ?: kotlin.run {
                setDefaultIcons(imageView)

            }
        }
    }

    private fun setDefaultIcons(view: ImageView) {

        if (PlaceHolder.placeHolderBitmap != null) { // Placeholder is bitmap
            view.setImageBitmap(PlaceHolder.placeHolderBitmap)
        } else if (PlaceHolder.placeHolderColor != -1) { // Placeholder is color
            view.setImageResource(PlaceHolder.placeHolderColor)
        }
    }


    fun loadJson() :InstaLoader{
        mUrl?.let {
            jsonResponseLiveData.postValue(ResponseState.loading)
            setJsonResponseListner()
            var requestBuilder = InstaLoaderRequestBuilder.Builder()
                .setUrl(it)
                .setMethodType(MethodType.GET)
                .setResponseType(ResponseType.JSONARRAY)
                .build()
            repository.loadJson(requestBuilder)
            R.string.app_name

        } ?: kotlin.run {
            jsonResponseLiveData.postValue(ResponseState.Error(getContext()?.getString(R.string.invalid_URL) ?: ""))

        }

        return  getInstaLoader()

    }

    private fun setJsonResponseListner() {
        repository.jsonResponseHandler = { status, json, message ->
            if (status) {

                json?.let { jsonResponseLiveData.postValue(ResponseState.Success(it)) }
                    ?:
                    kotlin.run { jsonResponseLiveData.postValue(ResponseState.Error("" + message)) }
            } else {
                jsonResponseLiveData.postValue(ResponseState.Error("" + message))

            }
        }

    }


    override fun evictAllBitmap() {
        repository.evictAllBitmap()
    }

    override fun evictAllJson() {
        repository.clearJson()
    }

    override fun cancelSingleRequest(url: String) {
        repository.cancelSingleRequest(url)
    }

    override fun cancelAllTask() {
        repository.cancelAllRequest()

    }


}

