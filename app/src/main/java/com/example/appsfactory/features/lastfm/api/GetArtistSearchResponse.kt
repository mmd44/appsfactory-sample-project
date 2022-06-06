package com.example.appsfactory.features.lastfm.api

import android.os.Parcelable
import com.example.appsfactory.features.lastfm.model.Artist
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GetArtistSearchResponse(
    val results: SearchResult
) : Parcelable {
    fun getMatchedArtists () = results.artistMatches.artists
}

@Parcelize
data class SearchResult(
    @Json(name = "artistmatches")
    val artistMatches: ArtistMatches
) : Parcelable

@Parcelize
data class ArtistMatches(
    @Json(name = "artist")
    val artists: List<Artist>
) : Parcelable