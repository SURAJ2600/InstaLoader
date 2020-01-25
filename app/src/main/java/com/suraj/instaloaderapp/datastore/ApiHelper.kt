package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.Observable

interface ApiHelper {
    fun getPinViewData(id:String) : Observable<List<PinView>>
}