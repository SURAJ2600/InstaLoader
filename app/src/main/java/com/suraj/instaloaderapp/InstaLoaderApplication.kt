package com.suraj.instaloaderapp

import android.app.Application
import com.suraj.instaloaderapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InstaLoaderApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InstaLoaderApplication)
            modules(AppModule)

        }
    }
}