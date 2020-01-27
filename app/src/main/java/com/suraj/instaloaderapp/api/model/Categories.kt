package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName
/**
 * @author Suraj s
 *
 * Data class for holding the value from server
 *
 * */
data class Categories(
    @SerializedName("id")
    var id: String ="",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("photo_count")
    var photo_count: String = "",
    @SerializedName("links")
    var links: Links=Links())
