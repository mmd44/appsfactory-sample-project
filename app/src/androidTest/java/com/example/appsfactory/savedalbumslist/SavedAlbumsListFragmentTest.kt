package com.example.appsfactory.data.savedalbumslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.appsfactory.R
import com.example.appsfactory.data.AlbumsRepositoryTest
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.model.Artist
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumsListFragment
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumsListFragmentDirections
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumsListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class SavedAlbumsListFragmentTest : AutoCloseKoinTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appContext: Application

    private val repository: AlbumsRepositoryTest by inject()

    val modules = module {
        stopKoin() // Stop the original app koin
        appContext = ApplicationProvider.getApplicationContext()
        viewModel {
            AlbumsListViewModel(
                appContext,
                get() as AlbumsRepositoryTest
            )
        }

        single { AlbumsRepositoryTest() }
    }

    @Before
    fun init() {
        startKoin {
            modules(listOf(modules))
        }
        repository.getStoredAlbumsLiveData()
    }

    @Test
    fun clickTask_verifyCountAndNavigateToSearchFragment() = runTest {

        val album1 = Album("Album 1", 100, "www.album.com", Artist("artist1",null), null)
        repository.addAlbums(album1)
        val navController = Mockito.mock(NavController::class.java)

        // GIVEN - On the albums list screen
        val scenario = launchFragmentInContainer<AlbumsListFragment>(Bundle(), R.style.AppTheme)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // THEN - Verify the items count
        Espresso.onView(withId(R.id.albums_recycler))
            .check(ViewAssertions.matches(ViewMatchers.hasChildCount(1)))

        // THEN - Verify that item exists with set text (workaround)
        Espresso.onView(withId(R.id.albums_recycler))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(withText("Album 1")), ViewActions.scrollTo()
                ))

        // WHEN we click an item
        Espresso.onView(withId(R.id.albums_recycler))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(withText("Album 1")), ViewActions.click()
                ))

        // THEN - Verify that we navigate to the details screen
        Mockito.verify(navController).navigate(
            AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(
                album1
            ))


        // WHEN we click the search button
        Espresso.onView(withId(R.id.search_fab))
            .perform(ViewActions.click())

        // THEN - Verify that we navigate to the search screen
        Mockito.verify(navController).navigate(
            AlbumsListFragmentDirections.actionAlbumsListFragmentToSearchFragment())
    }

    @Test
    fun invalidateShowNoData_verifyNoDataDisplayed () {
        // GIVEN - On the albums list screen with empty repo
        launchFragmentInContainer<AlbumsListFragment>(Bundle(), R.style.AppTheme)

        // THEN - Verify that no albums is shown
        Espresso.onView(withText(R.string.no_saved_albums))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}