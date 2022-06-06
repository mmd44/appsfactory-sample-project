package com.example.appsfactory.db.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Singleton class that is used to create the db
 */
object LocalDB {

    fun createDB(context: Context): RoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlbumsDatabase::class.java, "lastfm.db"
        ).build()
    }

}