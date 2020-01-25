package com.suraj.instaloader.manager

import org.json.JSONArray




/*Created by suraj on 25/01/2020
*
* This sealed class is used for handling the JSON response request from InstaLoader.
*
* */

sealed class ResponseState{

    object loading:ResponseState()

    data class Error(val error: String) : ResponseState()

    data class Success(val jsonArray: JSONArray) : ResponseState()



}
