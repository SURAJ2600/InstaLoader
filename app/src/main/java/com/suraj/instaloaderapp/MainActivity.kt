package com.suraj.instaloaderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloader.manager.ResponseState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //InstaLoader.getInstance(this , CACHE_SIZE)
        InstaLoader.init(this)
      InstaLoader.getInstance().source("https://pastebin.com/raw/wgkJgazE").loadJson()
//            .getJsonArrayResponse().observe(this, Observer {
//                when(it){
//                    is ResponseState.loading -> {
//                        Log.e("Response",">>>>>>>"+it)
//
//                    }
//                    is ResponseState.Error->{
//
//                    }
//                    is ResponseState.Success->{
//                        Log.e("Response",">>>>>>>"+it.jsonArray)
//
//
//                    }
//
//
//                }
//            })

        InstaLoader.getInstance()
            .source("https://images.unsplash.com/profile-1459198013794-1b8e57737f57?ixlib=rb-0.3.5\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026fit=crop\u0026h=128\u0026w=128\u0026s=03cfd5d0ceeb58f8a2c7fc3a42afead4")
           .placeholder(R.mipmap.ic_launcher)
           .into(imag)
        InstaLoader.getInstance()
            .source("https://images.unsplash.com/profile-1459198013794-1b8e57737f57?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=128\\u0026w=128\\u0026s=03cfd5d0ceeb58f8a2c7fc3a42afead4")
            .placeholder(R.mipmap.ic_launcher)
            .into(imgs)

      //  InstaLoader.getInstance().source("https://pastebin.com/raw/wgkJgazE").loadJson()



//
//        InstaLoader.with(this)
//            .source("https://pastebin.com/raw/wgkJgazE")
//            .loadJson()
//        InstaLoader.with(this)
//            .source("https://pastebin.com/raw/wgkJgazE")
//            .loadJson()
//        InstaLoader.with(this).cancelSingleTask("")
//        InstaLoader.with(this).cancelAllTask()
        imgs.setOnClickListener {
            startActivity(
                Intent(this, Main2Activity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            )
        }
    }


}
