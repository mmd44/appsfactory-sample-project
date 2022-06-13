package com.example.appsfactory.features.lastfm.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.appsfactory.base.BaseViewModel
import com.example.appsfactory.features.lastfm.data.AlbumDataSource
import com.example.appsfactory.features.lastfm.data.AlbumRepository
import com.example.appsfactory.features.lastfm.model.Artist


class SearchViewModel(
    val app: Application,
    private val dataSource: AlbumDataSource,
) : BaseViewModel(app) {
    fun loadArtists(query: String) : LiveData<PagingData<Artist>> = dataSource.searchForArtists(query)
}