package com.example.appsfactory.features.lastfm.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val name: String,
    val image: List<ItemImage>?
) : Parcelable {
    fun getMediumImageUrl () : String? {
        return if (image?.isNotEmpty() == true && image.size > 1) image[1].url
        else null
    }
}

@Parcelize
data class ItemImage (
    @Json(name = "#text")
    val url: String,
    val size: String
) : Parcelable