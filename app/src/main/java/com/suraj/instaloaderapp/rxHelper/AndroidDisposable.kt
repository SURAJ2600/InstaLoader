package com.suraj.instaloaderapp.rxHelper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/*
*
* A common class for handling Android disposable
* */
class AndroidDisposable {
    var compositeDisposable: CompositeDisposable?=null

    fun add(disposable: Disposable){
        compositeDisposable?.let {
            compositeDisposable?.add(disposable)

        } ?:kotlin.run {
            compositeDisposable= CompositeDisposable()
            compositeDisposable?.add(disposable)

        }
    }

    fun dispose(){
        compositeDisposable?.dispose()
        compositeDisposable=null
    }
}