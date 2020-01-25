package com.suraj.instaloader.bacgroundTasks

import java.util.concurrent.Callable



/*Created by suraj on 24/01/2020
*
* This is an abstract class for defining generic callable function.
* */
abstract class DownloadTask<T> : Callable<T> {
    abstract fun download(url: String): T
}