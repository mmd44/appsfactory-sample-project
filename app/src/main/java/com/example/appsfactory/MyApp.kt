package com.example.appsfactory

import android.app.Application
import com.example.appsfactory.db.local.AlbumsDatabase
import com.example.appsfactory.db.local.LocalDB
import com.example.appsfactory.features.lastfm.data.AlbumRepository
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.features.lastfm.ui.albumdetail.AlbumDetailsViewModel
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumsListViewModel
import com.example.appsfactory.features.lastfm.ui.search.SearchViewModel
import com.example.appsfactory.features.lastfm.ui.topalbums.TopAlbumsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        /**
         * Koin library as a service locator
         */
        val myModule = module {
            viewModel {
                AlbumsListViewModel(
                    get(),
                    get(),
                )
            }
            viewModel { (album: Album) ->
                AlbumDetailsViewModel(
                    get(),
                    get(),
                    album
                )
            }
            viewModel {
                SearchViewModel(
                    get(),
                    get(),
                )
            }
            viewModel { (artistName: String) ->
                TopAlbumsViewModel(
                    get(),
                    get(),
                    artistName
                )
            }

            single { AlbumRepository(get()) }
            single { LocalDB.createDB(this@MyApp) as AlbumsDatabase }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}