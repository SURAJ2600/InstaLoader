package com.suraj.instaloader.core

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor


/*Created by suraj on 25/01/2020
*
*
* Mainthread executor for running  task in ui thread
* */
class MainThreadExecutor : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}