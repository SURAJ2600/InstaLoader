package com.suraj.instaloaderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suraj.instaloader.InstaLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.imgs

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        imgs.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        InstaLoader.getInstance()
            .source("https://i.pinimg.com/originals/93/09/77/930977991c52b48e664c059990dea125.jpg")
            .placeholder(R.mipmap.ic_launcher)
            .into(imgs)

        InstaLoader.getInstance().source("https://pastebin.com/raw/wgkJgazE").loadJson()

    }
}
