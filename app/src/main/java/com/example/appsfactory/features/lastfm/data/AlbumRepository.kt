package com.example.appsfactory.features.lastfm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.appsfactory.base.Result
import com.example.appsfactory.db.local.AlbumsDao
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist
import com.example.appsfactory.features.lastfm.model.asDataBaseModel
import com.example.appsfactory.features.lastfm.model.asDomainModel
import com.example.appsfactory.network.LastFM.lastFMService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepository(
    private val dao: AlbumsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlbumDataSource {

    override fun searchForArtists(query: String): LiveData<PagingData<Artist>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 2
        ),
        pagingSourceFactory = {
            SearchPagingSource(query)
        }, initialKey = 1
    ).liveData


    override fun getStoredAlbumsLiveData(): LiveData<List<Album>> = Transformations.map(dao.getAlbums()) {
        it.asDomainModel()
    }

    override suspend fun getStoredAlbums(): Result<List<Album>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(dao.getSavedAlbums().asDomainModel())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getTopAlbums(artistName: String): Result<List<Album>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(lastFMService.topAlbumsAsync(artistName).await().topAlbums.albums)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveAlbum (album: Album): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            dao.saveAlbum(album.asDataBaseModel())
            Result.Success(true)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun removeAlbum (album: Album): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            dao.removeAlbum(album.asDataBaseModel())
            Result.Success(true)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getAlbumByArtistName (artistName: String): Result<Album> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(dao.getAlbumByArtistName(artistName).asDomainModel())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}