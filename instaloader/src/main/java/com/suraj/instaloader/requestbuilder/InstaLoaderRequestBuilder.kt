package com.suraj.instaloader.requestbuilder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView




/*Created by suraj on 24/01/2020
*
* A data class for holding the request from instaloader ,JSON or BITMAP,
*
*
* */

data class InstaLoaderRequestBuilder(
    val url:String,
    val enableCache:Boolean,
    val methodType: MethodType?,
    val responseType: ResponseType?,
    val bitmapReqWidth:Int,
    val bitmapReqHeight:Int,
    val bitmapConfig:Bitmap.Config,
    val bitmapOptions:BitmapFactory.Options,
    val imageScaleType:ImageView.ScaleType) {

    class Builder{
        private var url: String = ""
        private var enableCache:Boolean =true //setting true as default
        private var methodType: MethodType?=null //Method of  the request
        private var responseType: ResponseType?=null //Response Type
        private var bitmapReqWidth: Int =0 //Bitmap required width
        private var bitmapReqHeight: Int = 0 //Bitmap required height
        private var bitmapConfig:Bitmap.Config= Bitmap.Config.ARGB_8888 //Setting default config for bitmap
        private var bitmapOptions: BitmapFactory.Options = BitmapFactory.Options().apply {
            inPurgeable =true
        } //Setting default Bitmap.OPTION
        private var imageScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE //Seeting default scale type


        fun setUrl(url:String):Builder{
           this.url= url
            return this
        }
        fun setEnableCache(enableCache: Boolean):Builder{
            this.enableCache=enableCache
            return this
        }
        fun setMethodType(methodType:MethodType):Builder{
            this.methodType= methodType
            return this
        }
        fun setResponseType(responseType:ResponseType):Builder{
            this.responseType= responseType
            return this
        }
        fun setBitmapWidth(width:Int):Builder{
            this.bitmapReqWidth= width
            return this
        }

        fun setBitmapHeight(height:Int):Builder{
            this.bitmapReqHeight= height
            return this
        }
        fun setBitmapConfig(config:Bitmap.Config):Builder{
            this.bitmapConfig= config
            return this
        }
        fun setBitmapOption(options:BitmapFactory.Options):Builder{
            this.bitmapOptions= options
            return this
        }
        fun setScaleType(scaleType:ImageView.ScaleType):Builder{
            this.imageScaleType=scaleType
            return  this
        }
        fun build(): InstaLoaderRequestBuilder {
            return InstaLoaderRequestBuilder(url,enableCache, methodType, responseType, bitmapReqWidth, bitmapReqHeight, bitmapConfig, bitmapOptions, imageScaleType)
        }


    }
    companion object{
        val EMPTY =Builder().build()
    }
}