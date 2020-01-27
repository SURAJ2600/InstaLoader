package com.suraj.instaloaderapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InstaLoaderApplication :Application(), LifecycleObserver {

    companion object {
        private var instance: InstaLoaderApplication? = null

        fun applicationContext(): Context? {
            return instance?.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()

        InstaLoader.init(this)
        startKoin {
            androidContext(this@InstaLoaderApplication)
            modules(AppModule)
        }
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(this) // to observe Application lifecycle events
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroy() {

    }
}
