package com.suraj.instaloaderapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.ui.pinview.PinViewActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, PinViewActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
