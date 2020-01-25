package com.suraj.instaloader.manager

import org.json.JSONArray

sealed class ResponseState{

    object loading:ResponseState()

    data class Error(val error: String) : ResponseState()

    data class Success(val jsonArray: JSONArray) : ResponseState()



}
