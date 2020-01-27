package com.suraj.instaloaderapp.repo.mapper

import android.util.Log
import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.viewstate.PinViewState
import io.reactivex.functions.Function

/*
*
*
* Converter for converting the pinview data to its view state for handling the pinViewActivity view state
* */

class PinViewStateConverter : Function<List<PinView>, PinViewState> {
    override fun apply(pinViewList: List<PinView>): PinViewState {
        return PinViewState.Success(pinViewList)

    }
}