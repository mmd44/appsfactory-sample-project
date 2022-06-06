package com.example.appsfactory.features.lastfm.ui.savedalbums

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseViewModel
import com.example.appsfactory.base.Result
import com.example.appsfactory.features.lastfm.data.AlbumRepository
import com.example.appsfactory.features.lastfm.model.Album
import kotlinx.coroutines.launch


class AlbumsListViewModel(
    val app: Application,
    private val repository: AlbumRepository,
) : BaseViewModel(app) {

    var albumsList = repository.getStoredAlbumsLiveData()

    fun removeAlbum (album: Album) {
        album.isSaved = false
        showLoading.value = true
        viewModelScope.launch {
            val result = repository.removeAlbum(album)
            showLoading.postValue(false)
            when (result) {
                is Result.Success<Boolean> -> {
                    showSnackBar.value = app.getString(R.string.successfully_removed)
                }
            }
        }
    }

    /**
     * Inform the user that there's no stored data
     */
    fun invalidateShowNoData() {
        showNoData.value = albumsList.value == null || albumsList.value?.isEmpty() ?: true
    }

}