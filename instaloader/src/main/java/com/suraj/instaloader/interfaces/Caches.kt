package com.suraj.instaloader.interfaces

import android.graphics.Bitmap
import org.json.JSONObject

interface Caches {

    fun putImage(url: String, bitmap: Bitmap)

    fun getImage(url: String): Bitmap?

    fun clearImage()


    interface CacheJson {

        fun putJson(url: String, json: JSONObject)

        fun getJson(url: String): JSONObject?

        fun clearJson()
    }

}