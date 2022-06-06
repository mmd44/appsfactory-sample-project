package com.example.appsfactory.features.lastfm.ui.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseFragment
import com.example.appsfactory.base.NavigationCommand
import com.example.appsfactory.databinding.FragmentArtistsBinding
import com.example.appsfactory.utils.fadeIn
import com.example.appsfactory.utils.fadeOut
import com.example.appsfactory.utils.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException


class SearchFragment : BaseFragment() {

    override val _viewModel: SearchViewModel by viewModel()

    private lateinit var _binding: FragmentArtistsBinding

    private val adapter = SearchPagerAdapter(
        ArtistListener { artistName ->
            navigateToTopAlbums(artistName)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_artists, container, false
            )

        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        setHasOptionsMenu(true)
        setTitle(getString(R.string.search))

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAndObservers()
    }

    private fun setupRecyclerAndObservers() {
        _binding.artistsRecycler.layoutManager = LinearLayoutManager(this.context)
        _binding.artistsRecycler.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.NotLoading -> {
                    _binding.progressBarMain.fadeOut()
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                        _binding.noDataTextView.fadeIn()
                    } else {
                        _binding.noDataTextView.isVisible = false
                    }
                }
                loadState.refresh is LoadState.Loading -> _binding.progressBarMain.fadeIn()
                loadState.append is LoadState.Loading -> _binding.progressBarMain.fadeIn()
                else -> {
                    _binding.progressBarMain.fadeOut()
                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        val errMsg = if (it.error is UnknownHostException) getString(R.string.check_connectivity) else it.error.toString()
                        _viewModel.showErrorMessage.value = errMsg
                    }
                }
            }
        }
    }

    private fun navigateToTopAlbums(artistName: String) {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                SearchFragmentDirections.actionSearchFragmentToTopAlbumsFragment(artistName)
            )
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = getString(R.string.artist_name)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { searchQuery ->
                        _viewModel.loadArtists(searchQuery).observe(viewLifecycleOwner, {
                            it?.let {
                                adapter.submitData(lifecycle, it)
                            }
                        })
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
            searchView.isIconifiedByDefault = false
            searchView.onActionViewExpanded()
        }
    }
}
