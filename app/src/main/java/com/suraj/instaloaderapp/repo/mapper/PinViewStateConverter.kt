package com.suraj.instaloaderapp.repo.mapper

import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.viewstate.PinViewState
import io.reactivex.functions.Function

class PinViewStateConverter : Function<List<PinView>, PinViewState> {
    override fun apply(pinViewList: List<PinView>): PinViewState {

        return PinViewState.Success(pinViewList)


    }
}