package com.suraj.instaloaderapp.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.R

abstract class BaseActivity <T: ViewDataBinding,V: ViewModel> : AppCompatActivity() {
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null



    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBindingAndViewmodel();
    }
    fun getViewDataBinding(): T {
        return mViewDataBinding!!
    }

    private fun setBindingAndViewmodel() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding?.executePendingBindings()
    }
}