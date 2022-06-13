package com.example.appsfactory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.appsfactory.base.Result
import com.example.appsfactory.features.lastfm.data.AlbumDataSource
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist

// FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource (var albums: MutableList<Album>? = mutableListOf()) : AlbumDataSource {

    override fun searchForArtists(query: String): LiveData<PagingData<Artist>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTopAlbums(artistName: String): Result<List<Album>> {
        albums?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "No top albums", -1)
    }

    override fun getStoredAlbumsLiveData(): LiveData<List<Album>> {
        albums?.let { return MutableLiveData(albums) }
        return MutableLiveData(emptyList())
    }

    override suspend fun getStoredAlbums(): Result<List<Album>> {
        albums?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "No stored albums", -1)
    }

    override suspend fun saveAlbum(album: Album): Result<Boolean> {
        albums?.let {
            albums?.add(album)
            return Result.Success(true) }
        return Result.Error(
            "Failed to store album", -1)
    }

    override suspend fun removeAlbum(album: Album): Result<Boolean> {
        albums?.let {
            albums?.remove(album)
            return Result.Success(true) }
        return Result.Error(
            "Failed to remove album", -1)
    }

    override suspend fun getAlbumByArtistName(artistName: String): Result<Album> {
        val album = albums?.find {
                album -> album.artist.name == artistName
        }
        album?.let {
            return Result.Success(it)
        }
        return Result.Error(
            "Album with artist name $artistName not found", -1)
    }
}