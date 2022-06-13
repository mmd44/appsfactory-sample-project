package com.example.appsfactory.db.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.appsfactory.features.lastfm.model.Track
import java.io.ByteArrayOutputStream
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null) return null
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromTracksList(tracks: List<Track>?): String? {
        if (tracks == null) return null
        val gson = Gson()
        val type: Type = object : TypeToken<List<Track>?>() {}.type
        return gson.toJson(tracks, type)
    }

    @TypeConverter
    fun toTracksList(tracksStr: String?): List<Track>? {
        if (tracksStr == null) return null
        val gson = Gson()
        val type: Type = object : TypeToken<List<Track>?>() {}.type
        return gson.fromJson<List<Track>>(tracksStr, type)
    }
}