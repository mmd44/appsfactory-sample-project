package com.example.appsfactory.db.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appsfactory.features.lastfm.model.AlbumDB

/**
 * Data Access Object for the albums table.
 */
@Dao
interface AlbumsDao {

    @Query("SELECT * FROM albums")
    fun getAlbums(): LiveData<List<AlbumDB>>

    /**
     * @param artistName the artist name of the album
     * @return the album object
     */
    @Query("SELECT * FROM albums WHERE artist_name=:artistName")
    fun getAlbumByArtistName(artistName: String): AlbumDB

    /**
     * Insert an album in the database. If the album already exists, replace it.
     *
     * @param album the album to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlbum(album: AlbumDB)

    /**
     * Delete an album from the database.
     *
     * @param album the album to be removed.
     */
    @Delete
    suspend fun removeAlbum(album: AlbumDB)

    /**
     * Delete all albums.
     */
    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    /**
     * @return all albums.
     */
    @Query("SELECT * FROM albums")
    suspend fun getSavedAlbums(): List<AlbumDB>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg albums: AlbumDB)

}