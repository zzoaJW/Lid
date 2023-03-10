package com.z0o0a.lid.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.z0o0a.lid.R
import java.io.ByteArrayOutputStream

class RoomTypeConverter {
    @TypeConverter
    fun listToJson(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?): List<String>? {
        return Gson().fromJson(value,Array<String>::class.java)?.toList()
    }

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun bitmaptoByteArray(bitmap : Bitmap?) : ByteArray{
        if (bitmap == null){
            return byteArrayOf()
        }else{
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            return outputStream.toByteArray()
        }
    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun byteArraytoBitmap(bytes : ByteArray) : Bitmap? {
        if (bytes.isEmpty()){
            return null
        }else{
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}