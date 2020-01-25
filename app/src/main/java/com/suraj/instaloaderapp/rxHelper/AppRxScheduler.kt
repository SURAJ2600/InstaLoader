package com.suraj.instaloaderapp.rxHelper

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class AppRxScheduler :RxScheduler {
    override fun io(): Scheduler {
        return  Schedulers.io()
    }

    override fun compute(): Scheduler {

        return  Schedulers.computation()
    }

    override fun androidThread(): Scheduler {
        return AndroidSchedulers.mainThread()    }

    override fun internet(): Scheduler {
        return  INTERNET_SCHEDULERS



    }

    override fun runOnBackground(): Scheduler {

        return  BACKGROUND_SCHEDULERS
    }
    companion object{

        private var backgroundExecutor = Executors.newCachedThreadPool()
        var BACKGROUND_SCHEDULERS = Schedulers.from(backgroundExecutor)
        private var internetExecutor = Executors.newCachedThreadPool()
        var INTERNET_SCHEDULERS = Schedulers.from(internetExecutor)
    }
}