package com.suraj.instaloaderapp.datastore.model

/**
 * @author Suraj s
 *
 * data class for holding app specific data ,Build in Builder pattern ,so we can pass only the item we want
 *
 * */

class PinView(val id:String?,val name:String?,val likes:String?,var profileImage:String?){

    class  Builder{


        private var id:String?=""
        private var name:String? =""
        private var likes:String? = ""
        private var profileImage:String? = ""



        fun setId(id: String?):Builder{
            this.id=id
            return  this
        }
        fun setName(name:String?):Builder{
            this.name=name
            return  this
        }
        fun setLikes(likes:String?):Builder{
            this.likes=likes
            return  this
        }

        fun setProfileImage(profileImage: String?):Builder{
            this.profileImage=profileImage
            return this
        }

        fun build():PinView{
            return  PinView(id,name,likes,profileImage)
        }


    }
    companion object{
        val EMPTY =Builder().build()
    }





}
