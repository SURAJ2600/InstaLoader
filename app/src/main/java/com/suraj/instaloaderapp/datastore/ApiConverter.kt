package com.suraj.instaloaderapp.datastore

import android.util.Log
import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.functions.Function

class ApiConverter : Function<List<ApiResponse>, List<PinView>> {
    override fun apply(apiResponse: List<ApiResponse>): List<PinView> {
        val pinViewList = mutableListOf<PinView>()
        if (apiResponse == null || apiResponse.size == 0) {
            return pinViewList
        }
        for (i in 0..apiResponse.size-1){
            val pinView = PinView.Builder().setId(apiResponse.get(i).id ?: "")
                .setName(apiResponse.get(i).user.name ?: "")
                .setLikes(apiResponse.get(i).likes ?: "")
                .setProfileImage(apiResponse.get(i).user.profile_image.large).build()
            pinViewList.add(pinView)
        }


        return pinViewList

    }


}
