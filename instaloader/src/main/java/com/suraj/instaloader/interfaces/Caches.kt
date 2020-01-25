package com.suraj.instaloader.interfaces

import android.graphics.Bitmap
import org.json.JSONObject

/*Created by suraj on 25/01/2020
*
* Interface class for defining the common method for cache
* */

interface Caches {

    fun putImage(url: String, bitmap: Bitmap)

    fun getImage(url: String): Bitmap?

    fun evictAllBitmap()


    interface CacheJson {

        fun putJson(url: String, json: JSONObject)

        fun getJson(url: String): JSONObject?

        fun clearJson()
    }

}