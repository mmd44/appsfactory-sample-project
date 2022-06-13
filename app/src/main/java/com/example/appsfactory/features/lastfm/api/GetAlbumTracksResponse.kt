package com.example.appsfactory.features.lastfm.api

import android.os.Parcelable
import com.example.appsfactory.features.lastfm.model.Track
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GetAlbumTracksResponse(
    val album: AlbumDetails
) : Parcelable {
    fun getAlbumTracks () = album.tracks.track
}

@Parcelize
data class AlbumDetails(
    val tracks: AlbumTracks
) : Parcelable

@Parcelize
data class AlbumTracks(
    val track: List<Track>
) : Parcelable