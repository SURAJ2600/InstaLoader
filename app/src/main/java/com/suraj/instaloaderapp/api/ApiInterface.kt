package com.suraj.instaloaderapp.api

import com.suraj.instaloaderapp.api.model.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @GET("raw/{id}")
    fun getLoginDetails(@Path("id") user: String ) : Observable<List<ApiResponse>>
}