package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName
/**
 * @author Suraj s
 *
 * Data class for holding the value from server
 *
 * */
data class ProfileImage(
    @SerializedName("small")
    var small: String ="",
    @SerializedName("medium")
    var medium: String = "",
    @SerializedName("large")
    var large: String =""
)
