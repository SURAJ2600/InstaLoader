package com.suraj.instaloader.core

import android.os.Process

import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/*Create by suraj on 25/01/2020
*
* Executer suuplier is class for provinf the threadpool executer to the calling functions,
* In this way each task will execute and manage by the threadpool
*
* REFER @https://developer.android.com/training/multiple-threads/create-threadpool,
* @https://blog.mindorks.com/threadpoolexecutor-in-android-8e9d22330ee3
*
* */

class ExecutorSupplier/*
         * constructor for  DefaultExecutorSupplier
         */
private constructor() {
    private val forBackgroundTasks: ThreadPoolExecutor
    private val forLightWeightBackgroundTasks: ThreadPoolExecutor

    init {
        // setting the thread factory
        val backgroundPriorityThreadFactory =
            PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)

        // setting the thread pool executor for mForBackgroundTasks;
        forBackgroundTasks = ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            backgroundPriorityThreadFactory
        )
        forLightWeightBackgroundTasks = ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            backgroundPriorityThreadFactory
        )


    }

    fun forLightWeightBackgroundTasks(): ThreadPoolExecutor {
        return forLightWeightBackgroundTasks
    }

    /*
         * returns the thread pool executor for background task
         */
    fun forBackgroundTasks(): ThreadPoolExecutor {
        return forBackgroundTasks
    }



    companion object {
        /*
     * Number of cores to decide the number of threads
     */
        val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        private var instance: ExecutorSupplier? = null


        /*
     * returns the instance of DefaultExecutorSupplier
     */
        fun getInstance(): ExecutorSupplier {
            if (instance == null) {
                synchronized(ExecutorSupplier::class.java) {
                    instance = ExecutorSupplier()
                }

            }
            return instance!!
        }
    }


}