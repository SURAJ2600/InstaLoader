package com.suraj.instaloaderapp.di

import com.suraj.instaloaderapp.api.ApiInterface
import com.suraj.instaloaderapp.datastore.ApiConverter
import com.suraj.instaloaderapp.datastore.Remote
import com.suraj.instaloaderapp.repo.PinViewRepository
import com.suraj.instaloaderapp.repo.mapper.PinViewStateConverter
import com.suraj.instaloaderapp.rxHelper.AndroidDisposable
import com.suraj.instaloaderapp.rxHelper.AppRxScheduler
import com.suraj.instaloaderapp.ui.pinview.PinViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * @author  suraj s
 *
 * Providing dependency of viewmodel,repository  and dependent modules
 *
 *
 * */
var PinViewModule= module {

    single { PinViewStateConverter() }
    single { ApiConverter() }

    single { Remote(get() as ApiInterface, get()) }
    single { PinViewRepository(get(), get()) }


    viewModel {
        PinViewModel(
            get(),
            AppRxScheduler(),
            AndroidDisposable()
        )
    }
    /**
     * Login viewModel
     */
}