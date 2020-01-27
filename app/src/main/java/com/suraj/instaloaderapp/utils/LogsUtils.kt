package com.suraj.instaloaderapp.utils

import android.os.Build
import android.util.Log
import com.suraj.instaloaderapp.BuildConfig

/*create by Suraj on 27/01/2020
* @LogsUtils is the common class for logcat calls
* */
enum class LOGTYPE {
    E,D,W,I,V
}
object LogsUtils{

    fun showLog(TAG : String,message:String,type: LOGTYPE)
    {
        if(BuildConfig.DEBUG) {
            when (type) {
                //Debug
                LOGTYPE.D -> {
                    Log.d(TAG,message)
                }
                //Error
                LOGTYPE.E -> {
                    Log.e(TAG,message)
                }
                //informational
                LOGTYPE.I -> {
                    Log.i(TAG,message)
                }
                //Verbose
                LOGTYPE.V -> {
                    Log.v(TAG,message)
                }
                //warnings
                LOGTYPE.W -> {
                    Log.w(TAG,message)
                }
            }
        }
    }
}
