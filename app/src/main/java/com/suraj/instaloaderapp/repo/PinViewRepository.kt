package com.suraj.instaloaderapp.repo

import com.suraj.instaloaderapp.datastore.Remote
import com.suraj.instaloaderapp.repo.mapper.PinViewStateConverter
import com.suraj.instaloaderapp.viewstate.PinViewState
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

class PinViewRepository(var remoteHelper:Remote,var pinViewConverter: PinViewStateConverter) {



    fun getPinViewData(id:String) :Observable<PinViewState>{
     return   remoteHelper.getPinViewData(id)
            .map(pinViewConverter)
            .startWith(PinViewState.Loading)
            .compose(PinViewStateErrorConverter())
    }

    class PinViewStateErrorConverter :
        ObservableTransformer<PinViewState, PinViewState> {
        override fun apply(upstream: Observable<PinViewState>): ObservableSource<PinViewState> {
            return upstream.onErrorResumeNext(Function<Throwable, ObservableSource<PinViewState>> { cause ->
                Observable.just(convertToCause(cause))
            })
        }

        private fun convertToCause(cause: Throwable): PinViewState {
            return when (cause) {
                is IOException -> PinViewState.Error("JSON EXCEPTION")
                is UnknownHostException -> PinViewState.Error("NO INTERNET")
                is retrofit2.adapter.rxjava2.HttpException ->  PinViewState.Error("UNKNOWN")
                is HttpException ->  PinViewState.Error("Please use a valid URL")
                else -> PinViewState.Error("NO DATA")
            }
        }
    }


}