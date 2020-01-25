package com.suraj.instaloaderapp.ui.pinview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suraj.instaloaderapp.repo.PinViewRepository
import com.suraj.instaloaderapp.rxHelper.AndroidDisposable
import com.suraj.instaloaderapp.rxHelper.AppRxScheduler
import com.suraj.instaloaderapp.viewstate.PinViewState

class PinViewModel(val repository: PinViewRepository,val appRxScheduler: AppRxScheduler,var androidDisposable: AndroidDisposable) :ViewModel(){



    private var pinViewLiveData = MutableLiveData<PinViewState>()
    fun pinViewResponse(): LiveData<PinViewState> = pinViewLiveData


    fun getPinView( id:String ){
        androidDisposable.add(repository.getPinViewData(id)
            .subscribeOn(appRxScheduler.io())
            .observeOn(appRxScheduler.androidThread()).subscribe{pinViewLiveData.postValue(it)
            })



    }

}