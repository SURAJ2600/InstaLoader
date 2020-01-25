package com.suraj.instaloader.cache



/*Created by suraj on 25/01/2020
*
*
* */
interface CachEvict {


    fun evictAllBitmap()

    fun evictAllJson()

    fun cancelSingleRequest(url:String)

    fun cancelAllTask()

}