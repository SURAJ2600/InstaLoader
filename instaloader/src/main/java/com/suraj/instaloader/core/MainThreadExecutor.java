package com.suraj.instaloader.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;



/*Created by suraj on 25/01/2020
*
*
* Mainthread executor for running  task in ui thread
* */
public class MainThreadExecutor implements Executor {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}