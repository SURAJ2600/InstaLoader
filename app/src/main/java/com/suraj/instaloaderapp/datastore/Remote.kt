package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.ApiInterface
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.Observable
/**
 * @author  suraj s
 *
 * Remote class is used to fetch api from the interface
 *
 *
 * */
class Remote(var apiInterface: ApiInterface,var apiConverter: ApiConverter) :ApiHelper{
    override fun getPinViewData(id:String): Observable<List<PinView>> {
       return apiInterface.getPinViewFromApi(id)
            .map(apiConverter)
    }
}