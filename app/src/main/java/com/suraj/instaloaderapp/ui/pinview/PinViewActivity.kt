package com.suraj.instaloaderapp.ui.pinview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.BR

import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.databinding.ActivityPinViewBinding
import com.suraj.instaloaderapp.ui.base.BaseActivity
import com.suraj.instaloaderapp.utils.AppConstants
import com.suraj.instaloaderapp.utils.isNetworkAvailable
import com.suraj.instaloaderapp.utils.snackbarMessage
import com.suraj.instaloaderapp.viewstate.PinViewState
import org.koin.android.viewmodel.ext.android.viewModel



/**
 * @author suraj s
 * @sample This is an sample activity to demonstrate  the use of InstaLoader library
*
* */
class PinViewActivity : BaseActivity<ActivityPinViewBinding, PinViewModel>() {


    val pinviewModel: PinViewModel by viewModel()
    lateinit var dataBinding: ActivityPinViewBinding

    private lateinit var adapter: PinViewAdapter

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_view
    }

    override fun getViewModel(): PinViewModel {
        return pinviewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = getViewDataBinding()
        setUpRecylerView()
        observeViewModel()
        setSwipeRefresh()
        callPinView()
        callJsonFromInstaLoader()



    }



    /*
    *
    * An sample function to demonstrate. how json can be cached with @InstaLoader
    *
    * Please use debug mode for getting the LOGCAT messages
    *
    * */

    private fun callJsonFromInstaLoader() {

        InstaLoader.getInstance().source(AppConstants.JSON_URL)
            .loadJson().getJsonArrayResponse().observe(this, Observer {
                Log.e("Respose",">>>>"+it)
            })


    }



    /*
    *
    * Calling the piv view list with user id
    *
    * */

    private fun callPinView() {
        dataBinding.swipeRefresh.isRefreshing=false
        if (isNetworkAvailable(this)) {
            pinviewModel.getPinView(AppConstants.USER_ID)
        } else {
            snackbarMessage(getString(R.string.enable_internet))
            setNoInternet(true)
        }
    }




    /*
    *
    *
    * Swipe refresh call
    * */


    private fun setSwipeRefresh() {
        dataBinding.swipeRefresh.setOnRefreshListener {
            dataBinding.swipeRefresh.isRefreshing=true
            callPinView()
        }

    }




    /*
    *
    * Setup the recyler view
    * */
    private fun setUpRecylerView() {
        adapter = PinViewAdapter()
        dataBinding.rvPins.adapter = adapter
    }


    /*
    *
    * Observing the viewmodel for view state updates
    * */

    private fun observeViewModel() {
        with(pinviewModel) {

            pinViewResponse().observe(this@PinViewActivity, Observer {
                when (it) {
                    is PinViewState.Loading -> {
                        dataBinding.progress.visibility = View.VISIBLE
                        setNoInternet(false)
                        setNoData(false)
                    }
                    is PinViewState.Error -> {
                        dataBinding.progress.visibility = View.GONE
                        snackbarMessage(it.errorMessage)
                        setNoInternet(false)
                        setNoData(true)


                    }
                    is PinViewState.Success -> {
                        dataBinding.progress.visibility = View.GONE
                        setNoInternet(false)
                        setNoData(false)
                        adapter.addNews(it.list.toMutableList() ?: mutableListOf())

                    }


                }

            })
        }
    }


    /*
    * setting no internet view
    *
    * */

    fun setNoInternet(status: Boolean) {
        if (status) {
            dataBinding.imgOffline.visibility = View.VISIBLE
        } else {
            dataBinding.imgOffline.visibility = View.GONE

        }


    }

    /*
    *
    * Setting no data view
    *
    * */

    fun setNoData(status: Boolean) {
        if (status) {
            dataBinding.imgNoData.visibility = View.VISIBLE
        } else {
            dataBinding.imgNoData.visibility = View.GONE

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        /*
        *
        * We can also evict all bitmap cache and json by calling
        * InstaLoader.getInstance().evictAllBitmap()
          InstaLoader.getInstance().evictAllJson()
        *
        *
        * */




    }
}
