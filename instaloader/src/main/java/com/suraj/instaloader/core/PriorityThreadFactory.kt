package com.suraj.instaloader.core

import android.os.Process

import java.util.concurrent.ThreadFactory


/*Created by suraj on 25/01/2020
*
* PriorityThreadFactory is use of setting the priority
*
* */

class PriorityThreadFactory(private val mThreadPriority: Int) : ThreadFactory {

    override fun newThread(runnable: Runnable): Thread {
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(mThreadPriority)
            } catch (t: Throwable) {

            }

            runnable.run()
        }
        return Thread(wrapperRunnable)
    }

}
