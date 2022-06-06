package com.example.appsfactory.features.lastfm.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMService {


    @GET("2.0/")
    fun searchForArtistsAsync(
        @Query("artist") artist: String,
        @Query("page") page: Int? = 1,
        @Query("method") method: String? = "artist.search",
        @Query("format") format: String? = "json",
    ): Deferred<GetArtistSearchResponse>

    @GET("2.0/")
    fun topAlbumsAsync(
        @Query("artist") artist: String,
        @Query("method") method: String? = "artist.gettopalbums",
        @Query("format") format: String? = "json",
    ): Deferred<GetTopAlbumsResponse>
}