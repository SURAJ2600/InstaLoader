package com.suraj.instaloaderapp.ui.fullview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.R
import kotlinx.android.synthetic.main.activity_full_image_view.*

class FullImageViewActivity : AppCompatActivity() {
    companion object {
        const val imageUrl = "url"

        fun newIntent(context: Context, url: String): Intent {
            return Intent(context, FullImageViewActivity::class.java)
                .putExtra(imageUrl, url)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_view)
        var url = if (intent.extras != null) {
            intent.getStringExtra(imageUrl) ?: ""
        } else {
            ""
        }

        InstaLoader.getInstance().source(url)
            .into(imgFull)

        ll_back.setOnClickListener {
            finish()
        }
    }
}
