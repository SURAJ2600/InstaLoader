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
            .source("https://i.pinimg.com/originals/93/09/77/930977991c52b48e664c059990dea125.jpg")
           .placeholder(R.mipmap.ic_launcher)
           .into(imag)
        InstaLoader.getInstance()
            .source("https://i.pinimg.com/originals/93/09/77/930977991c52b48e664c059990dea125.jpg")
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
