package com.example.appsfactory.savedalbumslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appsfactory.MainCoroutineRule
import com.example.appsfactory.data.FakeDataSource
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumsListViewModel
import com.example.appsfactory.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SavedAlbumsListViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var albumsListViewModel: AlbumsListViewModel

    private lateinit var albumsDataSource: FakeDataSource

    @Before
    fun setupViewModel() {
        // We initialise to 2 albums
        albumsDataSource = FakeDataSource()
        val album1 = Album("Album1", 100, "www.album.com", Artist("artist1",null), null)
        val album2 = Album("Album2", 0, "", Artist("artist2",null), null)

        runBlocking {
            albumsDataSource.saveAlbum(album1)
            albumsDataSource.saveAlbum(album2)
        }

        albumsListViewModel =  AlbumsListViewModel (ApplicationProvider.getApplicationContext(), albumsDataSource)
    }

    @Test
    fun loadAlbums(){
        albumsListViewModel.albumsList
        MatcherAssert.assertThat(
            albumsListViewModel.albumsList.getOrAwaitValue().size,
            Is.`is`(2)
        )
    }
}