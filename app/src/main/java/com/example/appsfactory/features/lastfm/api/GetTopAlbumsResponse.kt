package com.example.appsfactory.features.lastfm.api

import android.os.Parcelable
import com.example.appsfactory.features.lastfm.model.Album
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GetTopAlbumsResponse(
    @Json(name = "topalbums")
    val topAlbums: TopAlbums
) : Parcelable

@Parcelize
data class TopAlbums(
    @Json(name = "album")
    val albums: List<Album>
) : Parcelable
