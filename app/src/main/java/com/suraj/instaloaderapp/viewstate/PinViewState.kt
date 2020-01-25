package com.suraj.instaloaderapp.viewstate

import com.suraj.instaloaderapp.datastore.model.PinView

sealed class PinViewState {
    object Loading:PinViewState()
    data class Success(var list: List<PinView>):PinViewState()
    data class Error (var errorMessage:String) :PinViewState()
}