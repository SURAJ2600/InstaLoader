package com.suraj.instaloader.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView

import java.io.IOException
import java.net.FileNameMap
import java.net.URLConnection

import okhttp3.Response
import okio.Okio


object Utils {


    fun decodeBitmap(
        response: Response, maxWidth: Int,
        maxHeight: Int, decodeConfig: Bitmap.Config,
        scaleType: ImageView.ScaleType
    ): Bitmap? {
        return decodeBitmap(
            response, maxWidth, maxHeight, decodeConfig,
            BitmapFactory.Options(), scaleType
        )
    }

    fun decodeBitmap(
        response: Response, maxWidth: Int,
        maxHeight: Int, decodeConfig: Bitmap.Config,
        decodeOptions: BitmapFactory.Options,
        scaleType: ImageView.ScaleType
    ): Bitmap? {
        var data = ByteArray(0)
        try {
            data = Okio.buffer(response.body()!!.source()).readByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var bitmap: Bitmap? = null
        if (maxWidth == 0 && maxHeight == 0) {
            decodeOptions.inPreferredConfig = decodeConfig
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, decodeOptions)
        } else {
            decodeOptions.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, decodeOptions)
            val actualWidth = decodeOptions.outWidth
            val actualHeight = decodeOptions.outHeight

            val desiredWidth = getResizedDimension(
                maxWidth, maxHeight,
                actualWidth, actualHeight, scaleType
            )
            val desiredHeight = getResizedDimension(
                maxHeight, maxWidth,
                actualHeight, actualWidth, scaleType
            )

            decodeOptions.inJustDecodeBounds = false
            decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight)
            val tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.size, decodeOptions)

            if (tempBitmap != null && (tempBitmap.width > desiredWidth || tempBitmap.height > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(
                    tempBitmap,
                    desiredWidth, desiredHeight, true
                )
                tempBitmap.recycle()
            } else {
                bitmap = tempBitmap
            }
        }

        if (bitmap == null) {
            // return ANResponse.failed(Utils.getErrorForParse(new ANError(response)));
        } else {
            //return ANResponse.success(bitmap);
        }
        return bitmap
    }

    private fun getResizedDimension(
        maxPrimary: Int, maxSecondary: Int,
        actualPrimary: Int, actualSecondary: Int,
        scaleType: ImageView.ScaleType
    ): Int {

        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary
        }

        if (scaleType == ImageView.ScaleType.FIT_XY) {
            return if (maxPrimary == 0) {
                actualPrimary
            } else maxPrimary
        }

        if (maxPrimary == 0) {
            val ratio = maxSecondary.toDouble() / actualSecondary.toDouble()
            return (actualPrimary * ratio).toInt()
        }

        if (maxSecondary == 0) {
            return maxPrimary
        }

        val ratio = actualSecondary.toDouble() / actualPrimary.toDouble()
        var resized = maxPrimary

        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if (resized * ratio < maxSecondary) {
                resized = (maxSecondary / ratio).toInt()
            }
            return resized
        }

        if (resized * ratio > maxSecondary) {
            resized = (maxSecondary / ratio).toInt()
        }
        return resized
    }

    fun findBestSampleSize(
        actualWidth: Int, actualHeight: Int,
        desiredWidth: Int, desiredHeight: Int
    ): Int {
        val wr = actualWidth.toDouble() / desiredWidth
        val hr = actualHeight.toDouble() / desiredHeight
        val ratio = Math.min(wr, hr)
        var n = 1.0f
        while (n * 2 <= ratio) {
            n *= 2f
        }
        return n.toInt()
    }


}
