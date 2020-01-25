package com.suraj.instaloaderapp.rxHelper

import io.reactivex.Scheduler

interface RxScheduler {
    fun runOnBackground(): Scheduler
    fun io(): Scheduler
    fun compute(): Scheduler
    fun androidThread(): Scheduler
    fun internet(): Scheduler
}