package com.example.appsfactory.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.appsfactory.features.lastfm.model.AlbumDB

/**
 * The Room Database that contains the albums table.
 */
@Database(entities = [AlbumDB::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AlbumsDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumsDao
}