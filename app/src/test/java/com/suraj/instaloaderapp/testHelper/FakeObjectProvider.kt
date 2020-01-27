package com.suraj.instaloaderapp.testHelper

import com.google.gson.annotations.SerializedName
import com.suraj.instaloaderapp.api.model.*
import com.suraj.instaloaderapp.datastore.model.PinView

object FakeObjectProvider {



    var id = "id"
    var name = "name"
    var likes= "likes"
    var profile_image= "profile_image"
    var profileImage= ProfileImage("link","link","link")
    var user=User("","",name,profileImage)

    fun makeApiData():ApiResponse{
        return  ApiResponse(id, "", "","","",likes,"",user,null,null,null)
    }

    fun makeUserViewData():PinView{
        return  PinView(id, name, likes, profile_image)
    }
}
