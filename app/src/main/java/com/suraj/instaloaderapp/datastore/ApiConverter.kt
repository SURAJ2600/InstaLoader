package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import io.reactivex.functions.Function

class ApiConverter : Function<List<ApiResponse>, List<PinView>> {
    override fun apply(apiResponse: List<ApiResponse>): List<PinView> {
        val pinViewList = mutableListOf(PinView.EMPTY)
        if (apiResponse == null || apiResponse.size == 0) {
            return pinViewList
        }

        for (data in apiResponse) {
            val pinView = PinView.Builder().setId(data.id ?: "")
                .setName(data.user.name ?: "")
                .setLikes(data.likes ?: "")
                .setProfileImage(data.user.profile_image.medium).build()
            pinViewList.add(pinView)

        }

        return pinViewList

    }


}
