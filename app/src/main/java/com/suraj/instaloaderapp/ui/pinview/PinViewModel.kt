package com.suraj.instaloaderapp.ui.pinview

import androidx.lifecycle.ViewModel
import com.suraj.instaloaderapp.repo.PinViewRepository
import com.suraj.instaloaderapp.rxHelper.AndroidDisposable
import com.suraj.instaloaderapp.rxHelper.AppRxScheduler

class PinViewModel(val repository: PinViewRepository,val appRxScheduler: AppRxScheduler,var androidDisposable: AndroidDisposable) :ViewModel(){
}