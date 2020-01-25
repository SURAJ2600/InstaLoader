package com.suraj.instaloader.cache



/*Created by suraj on 25/02/2020*/
/*
* This is the default configuration class for specifying the cache size
*
* for the InstaLoader module.....
* */
class Config {
    companion object {
        val maxMemory = Runtime.getRuntime().maxMemory() /1024
        val defaultCacheSize = (maxMemory/4).toInt()
    }
}