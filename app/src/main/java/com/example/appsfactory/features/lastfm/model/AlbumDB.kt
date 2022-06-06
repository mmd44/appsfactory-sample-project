package com.example.appsfactory.features.lastfm.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Immutable model class for a Album. In order to compile with Room
 *
 * @param name          name of the album
 * @param playCount     number of times played
 * @param url           album url
 * @param artistName    artist name
 * @param id            id of the album
 */
@Entity(tableName = "albums")
data class AlbumDB(
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "play_count") var playCount: Int,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "artist_name") var artistName: String,
    @ColumnInfo(name = "album_image") var albumImage: Bitmap?
)

fun List<Album>.asDatabaseModel(): Array<AlbumDB> {
    return this.map {
        it.asDataBaseModel()
    }.toTypedArray()
}

fun Album.asDataBaseModel(): AlbumDB {
    return AlbumDB(
        name = this.name,
        playCount = this.playCount,
        url = this.url,
        artistName = this.artist.name,
        albumImage = this.imageBitmap
    )
}

fun List<AlbumDB>.asDomainModel(): List<Album> {
    return map {
        it.asDomainModel()
    }
}

fun AlbumDB.asDomainModel(): Album {
    val album = Album(
        name = this.name,
        playCount = this.playCount,
        url = this.url,
        artist = Artist(name = artistName, image = null),
        image = null,
    )
    album.imageBitmap = this.albumImage
    return album
}
