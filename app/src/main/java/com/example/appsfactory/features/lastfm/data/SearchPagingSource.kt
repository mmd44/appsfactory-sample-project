package com.example.appsfactory.features.lastfm.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.appsfactory.features.lastfm.model.Artist
import com.example.appsfactory.network.LastFM.lastFMService

class SearchPagingSource(val searchQuery: String): PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val position = params.key ?: 1
            val response = lastFMService.searchForArtistsAsync(artist= searchQuery, page = position).await()
            LoadResult.Page(data = response.getMatchedArtists(), prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.getMatchedArtists().isEmpty()) null else position + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
