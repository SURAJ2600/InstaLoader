package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.ApiInterface
import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.Observable

class Remote(var apiInterface: ApiInterface,var apiConverter: ApiConverter) :ApiHelper{
    override fun getPinViewData(id:String): Observable<List<PinView>> {
       return apiInterface.getLoginDetails(id)
            .map(apiConverter)
    }
}