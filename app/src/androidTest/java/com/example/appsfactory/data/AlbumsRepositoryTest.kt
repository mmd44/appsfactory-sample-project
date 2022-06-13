package com.example.appsfactory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.appsfactory.base.Result
import com.example.appsfactory.features.lastfm.data.AlbumDataSource
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist
import kotlinx.coroutines.runBlocking


class AlbumsRepositoryTest : AlbumDataSource {

    var testError = false

    var albumsServiceData: LinkedHashMap<String, Album> = LinkedHashMap()

    private val observableAlbumsLiveData = MutableLiveData<List<Album>>()

    fun refreshAlbums() {
        observableAlbumsLiveData.postValue(albumsServiceData.values.toMutableList())
    }

    fun addAlbums(vararg albums: Album) {
        for (album in albums) {
            albumsServiceData[album.artist.name] = album
        }
        runBlocking { refreshAlbums() }
    }

    override suspend fun getTopAlbums(artistName: String): Result<List<Album>> {
        if (testError) return Result.Error("An error has occurred!")
        return Result.Success(albumsServiceData.values.toList())
    }


    override suspend fun getStoredAlbums(): Result<List<Album>> {
        return Result.Success(ArrayList(albumsServiceData.values))
    }

    override suspend fun saveAlbum(album: Album): Result<Boolean> {
        albumsServiceData[album.artist.name] = album
        return Result.Success(true)
    }

    override suspend fun removeAlbum(album: Album): Result<Boolean> {
        albumsServiceData[album.artist.name]?.let {
            albumsServiceData.remove(it.artist.name)
            return Result.Success(true)
        }
        return Result.Error(
            "Failed to remove album", -1
        )
    }

    override suspend fun getAlbumByArtistName(artistName: String): Result<Album> {
        albumsServiceData[artistName]?.let {
            albumsServiceData.remove(it.artist.name)
            return Result.Success(it)
        }
        return Result.Error(
            "Album with artist name $artistName not found", -1
        )
    }

    override fun searchForArtists(query: String): LiveData<PagingData<Artist>> {
        TODO("Not yet implemented")
    }

    override fun getStoredAlbumsLiveData(): LiveData<List<Album>> {
        return observableAlbumsLiveData
    }
}