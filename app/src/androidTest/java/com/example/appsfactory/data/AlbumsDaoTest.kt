package com.example.appsfactory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.appsfactory.db.local.AlbumsDatabase
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist
import com.example.appsfactory.features.lastfm.model.asDataBaseModel
import com.example.appsfactory.features.lastfm.model.asDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// Unit test the DAO
@SmallTest
class AlbumsDaoTest {

    private lateinit var database: AlbumsDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlbumsDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun saveAlbumAndGetByArtistName() = runTest() {

        // GIVEN we insert an album
        val album1 = Album("Album 1", 100, "www.album.com", Artist("artist1",null), null)
        database.albumDao().saveAlbum(album1.asDataBaseModel())

        // WHEN the album is retrieved by artist name from the database
        val loaded = database.albumDao().getAlbumByArtistName(album1.artist.name).asDomainModel()

        // THEN the loaded data should contain the expected values
        MatcherAssert.assertThat(loaded, IsNull.notNullValue())
        MatcherAssert.assertThat(loaded.name, `is` (album1.name))
        MatcherAssert.assertThat(loaded.playCount, `is` (album1.playCount))
        MatcherAssert.assertThat(loaded.url, `is` (album1.url))
        MatcherAssert.assertThat(loaded.artist.name, `is` (album1.artist.name))
        MatcherAssert.assertThat(loaded.image, `is` (nullValue()))
    }

    @Test
    fun getAlbums() = runTest {

        // GIVEN we save some albums
        val album1 = Album("Album 1", 100, "www.album.com", Artist("artist1",null), null)
        val album2 = Album("Album 2", 0, "", Artist("artist1",null), null)

        database.albumDao().saveAlbum(album1.asDataBaseModel())
        database.albumDao().saveAlbum(album2.asDataBaseModel())

        // WHEN we get all the albums
        val albumsList = database.albumDao().getSavedAlbums()

        // THEN the loaded list should be of size 2
        MatcherAssert.assertThat(albumsList.size, `is`(2))
    }

    @Test
    fun deleteAndGetAlbums() = runTest {

        // GIVEN we save an album
        val album1 = Album("Album 1", 100, "www.album.com", Artist("artist1",null), null)
        database.albumDao().saveAlbum(album1.asDataBaseModel())

        // WHEN we delete all the albums
        database.albumDao().deleteAllAlbums()
        val albumsList = database.albumDao().getSavedAlbums()

        // THEN the loaded data should be an empty list
        MatcherAssert.assertThat(albumsList, `is`(emptyList()))
    }


}