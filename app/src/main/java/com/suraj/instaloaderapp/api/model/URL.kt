package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName
/**
 * @author Suraj s
 *
 * Data class for holding the value from server
 *
 * */
data  class URL(
    @SerializedName("raw")
    var raw: String ="",
    @SerializedName("full")
    var full: String = "",
    @SerializedName("regular")
    var regular: String ="",
    @SerializedName("small")
    var small: String = "",
    @SerializedName("thumb")
    var thumb: String = "")