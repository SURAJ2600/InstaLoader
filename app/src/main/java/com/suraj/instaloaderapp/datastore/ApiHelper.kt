package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.Observable
/**
 * @author  suraj s created on 26/01/2020
 *
 * Helper interface for Remote class
 *
 *
 * */
interface ApiHelper {
    fun getPinViewData(id:String) : Observable<List<PinView>>
}