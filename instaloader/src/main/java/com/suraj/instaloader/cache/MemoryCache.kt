package com.suraj.instaloader.cache

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache
import com.suraj.instaloader.interfaces.Caches


/*Created bu suraj on 24/02/2012*/

/*A memory cache layer for cacheing the bitmap
* This class use the default LRU cache provided by Android Team
*
* */

class MemoryCache (newMaxSize: Int) : Caches {

    private val cache : LruCache<String, Bitmap>

    init {
        var cacheSize : Int
        if (newMaxSize > Config.maxMemory) {
            cacheSize = Config.defaultCacheSize
            Log.d("memory_cache","New value of cache is bigger than maximum cache available on system")
        } else {
            cacheSize = newMaxSize
        }
        cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return (value.rowBytes)*(value.height)/1024
            }
        }
    }

    override fun putImage(url: String, bitmap: Bitmap) {
        cache.put(url,bitmap)
    }

    override fun getImage(url: String): Bitmap? {
        return  cache.get(url)
    }

    override fun evictAllBitmap() {
        cache.evictAll()
    }






}