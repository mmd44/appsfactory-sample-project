package com.example.appsfactory.features.lastfm.ui.albumdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appsfactory.base.BaseViewModel
import com.example.appsfactory.features.lastfm.data.AlbumRepository
import com.example.appsfactory.features.lastfm.model.Album

class AlbumDetailsViewModel(
    val app: Application,
    private val repository: AlbumRepository,
    private val album: Album,
) : BaseViewModel(app) {

    private var _album = MutableLiveData<Album>()
    val storedAlbum: LiveData<Album>
        get() = _album
}