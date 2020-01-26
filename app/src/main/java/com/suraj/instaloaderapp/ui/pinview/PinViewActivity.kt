package com.suraj.instaloaderapp.ui.pinview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.BR

import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.databinding.ActivityPinViewBinding
import com.suraj.instaloaderapp.ui.base.BaseActivity
import com.suraj.instaloaderapp.viewstate.PinViewState
import org.koin.android.viewmodel.ext.android.viewModel

class PinViewActivity : BaseActivity<ActivityPinViewBinding, PinViewModel>() {


    val pinviewModel : PinViewModel by viewModel()
   lateinit var dataBinding:ActivityPinViewBinding

    private lateinit var adapter: PinViewAdapter

    override fun getBindingVariable(): Int { return  BR.viewModel  }

    override fun getLayoutId(): Int { return  R.layout.activity_pin_view }

    override fun getViewModel(): PinViewModel {return  pinviewModel}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InstaLoader.init(this)
        dataBinding=getViewDataBinding()
        setUpRecylerView()
        observeViewModel()
        getjson()
    }

    private fun getjson() {



//        for (i in 0..15) {
//            InstaLoader.getInstance().source("https://pastebin.com/raw/wgkJgazE").loadJson()
//                .getJsonArrayResponse()
//                .observe(this, Observer {
//                    Log.e("Json", ">>>" + it.toString())
//                })
//        }


    }

    private fun setUpRecylerView() {
        adapter= PinViewAdapter()
        dataBinding.rvPins.adapter=adapter
    }

    private fun observeViewModel() {
        with(pinviewModel){
            this.getPinView("wgkJgazE")

            pinViewResponse().observe(this@PinViewActivity, Observer {
                when(it)
                {
                    is PinViewState.Loading->{}
                    is PinViewState.Error ->{
                        Log.e("Error",">>>>>>>>>>"+it.errorMessage)
                    }
                    is PinViewState.Success->{
                        Log.e("Error",">>>>>>>>>>"+it.list)
                        adapter.addNews(it.list.toMutableList() ?: mutableListOf())

                    }


                }

            })





        }

    }
}
