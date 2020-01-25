package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("id")
    var id: String ="",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("photo_count")
    var photo_count: String = "",
    @SerializedName("links")
    var links: Links=Links())
