package com.suraj.instaloaderapp.api.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("id")
    var id: String ="",
    @SerializedName("created_at")
    var created_at: String = "",
    @SerializedName("width")
    var width: String ="",
    @SerializedName("height")
    var height: String = "",
    @SerializedName("color")
    var color: String ="",
    @SerializedName("likes")
    var likes: String = "",
    @SerializedName("liked_by_user")
    var liked_by_user: Any = "",
    @SerializedName("user")
    var user:User= User(),
    @SerializedName("urls")
    var urls:URL? = URL(),
    @SerializedName("categories")
    var categories:List<Categories>? = listOf(),
    @SerializedName("links")
    var links:Links?)



