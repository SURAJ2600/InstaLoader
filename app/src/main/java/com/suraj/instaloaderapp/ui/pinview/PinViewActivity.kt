package com.suraj.instaloaderapp.ui.pinview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suraj.instaloaderapp.BR
import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.databinding.ActivityPinViewBinding
import com.suraj.instaloaderapp.ui.base.BaseActivity

class PinViewActivity : BaseActivity<ActivityPinViewBinding, PinViewModel>() {
    override fun getBindingVariable(): Int {
    return  BR.viewModel
         }

    override fun getLayoutId(): Int {
    return  R.layout.activity_pin_view }

    override fun getViewModel(): PinViewModel {
      }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_view)
    }
}
