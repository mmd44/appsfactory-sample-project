package com.example.appsfactory.features.lastfm.ui.topalbums

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseViewModel
import com.example.appsfactory.base.Result
import com.example.appsfactory.features.lastfm.data.AlbumRepository
import com.example.appsfactory.features.lastfm.model.Album
import kotlinx.coroutines.launch

class TopAlbumsViewModel(
    val app: Application,
    private val repository: AlbumRepository,
    private val artistName: String,
) : BaseViewModel(app) {

    var savedAlbumsList: MutableList<Album> = mutableListOf()

    val topAlbumsList = MutableLiveData<List<Album>>()

    val saveStatus = MutableLiveData<Album>()

    init {
        viewModelScope.launch {
            val result = repository.getStoredAlbums()
            when (result) {
                is Result.Success<*> -> {
                    savedAlbumsList = result.data as MutableList<Album>
                }
                is Result.Error ->
                    showSnackBar.value = result.message
            }
        }
    }

    fun loadTopAlbums() {
        showLoading.value = true
        viewModelScope.launch {
            val result = repository.getTopAlbums(artistName)
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = result.data as List<Album>
                    topAlbumsList.value = dataList
                }
                is Result.Error ->
                    showSnackBar.value = result.message
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    fun checkIfSaved (album: Album) : Boolean {
        return savedAlbumsList.find { it.name == album.name } != null || album.isSaved == true
    }

    fun saveUnsaveAlbum(album: Album) {
        showLoading.value = true
        viewModelScope.launch {
            if (checkIfSaved(album)) {
                val result = repository.removeAlbum(album)
                showLoading.postValue(false)
                when (result) {
                    is Result.Success<Boolean> -> {
                        savedAlbumsList.removeAll {found -> found.name == album.name}
                        album.isSaved = false
                        saveStatus.postValue(album)
                        showSnackBar.value = app.getString(R.string.successfully_removed)
                    }
                    is Result.Error ->
                        showSnackBar.value = result.message
                }
            } else {
                val result = repository.saveAlbum(album)
                showLoading.postValue(false)
                when (result) {
                    is Result.Success<Boolean> -> {
                        album.isSaved = true
                        saveStatus.postValue(album)
                        showSnackBar.value = app.getString(R.string.successfully_saved)
                    }
                    is Result.Error ->
                        showSnackBar.value = result.message
                }
            }
        }
    }

    private fun invalidateShowNoData() {
        showNoData.value = topAlbumsList.value == null || topAlbumsList.value?.isEmpty() ?: true
    }
}