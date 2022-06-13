package com.example.appsfactory.features.lastfm.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Album(
    val name: String,
    @Json(name = "playcount")
    val playCount: Int,
    val url: String,
    val artist: Artist,
    val image: List<ItemImage>?
) : Parcelable {
    @Transient
    var isSaved: Boolean? = false
    @Transient
    var imageBitmap: Bitmap? = null
    @Transient
    var tracks: List<Track>? = null

    fun getMediumImageUrl(): String? {
        return if (image?.isNotEmpty() == true && image.size > 1) image[1].url
        else null
    }
}

