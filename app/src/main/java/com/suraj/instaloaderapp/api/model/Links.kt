package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName
/**
 * @author Suraj s
 *
 * Data class for holding the value from server
 *
 * */
data class Links( @SerializedName("self")
                  var self: String ="",
                  @SerializedName("html")
                  var html: String = "",
                  @SerializedName("photos")
                  var photos: String ="",
                  @SerializedName("likes")
                  var likes: String = "",
                  @SerializedName("download")
                  var download: String ="")