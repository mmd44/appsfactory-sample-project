package com.example.appsfactory.features.lastfm.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.appsfactory.base.Result
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist


interface AlbumDataSource {

    fun getStoredAlbumsLiveData(): LiveData<List<Album>>
    suspend fun getStoredAlbums(): Result<List<Album>>
    suspend fun saveAlbum (album: Album): Result<Boolean>
    suspend fun removeAlbum (album: Album): Result<Boolean>
    suspend fun getAlbumByArtistName (artistName: String): Result<Album>

    fun searchForArtists(query: String): LiveData<PagingData<Artist>>
    suspend fun getTopAlbums(artistName: String): Result<List<Album>>
}