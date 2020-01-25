package com.suraj.instaloader.core;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*Create by suraj on 25/01/2020
*
* Executer suuplier is class for provinf the threadpool executer to the calling functions,
* In this way each task will execute and manage by the threadpool
*
* REFER @https://developer.android.com/training/multiple-threads/create-threadpool,
* @https://blog.mindorks.com/threadpoolexecutor-in-android-8e9d22330ee3
*
* */

public class ExecutorSupplier{
    /*
     * Number of cores to decide the number of threads
     */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor forBackgroundTasks;
    private final Executor mainThreadExecutor;
    private static ExecutorSupplier instance;


    /*
     * returns the instance of DefaultExecutorSupplier
     */
    public static ExecutorSupplier getInstance() {
        if (instance == null) {
            synchronized (ExecutorSupplier.class) {
                instance = new ExecutorSupplier();
            }

        }
        return instance;
    }
        /*
         * constructor for  DefaultExecutorSupplier
         */
    private ExecutorSupplier() {
            // setting the thread factory
            ThreadFactory backgroundPriorityThreadFactory = new
                    PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

            // setting the thread pool executor for mForBackgroundTasks;
            forBackgroundTasks = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    backgroundPriorityThreadFactory
            );

        // setting the thread pool executor for mMainThreadExecutor;
        mainThreadExecutor = new MainThreadExecutor();

        }

        /*
         * returns the thread pool executor for background task
         */
        public ThreadPoolExecutor forBackgroundTasks () {
            return forBackgroundTasks;
        }

    public Executor forMainThreadTasks() {
        return mainThreadExecutor;
    }


}