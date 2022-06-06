package com.example.appsfactory.features.lastfm.ui.savedalbums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseFragment
import com.example.appsfactory.base.NavigationCommand
import com.example.appsfactory.databinding.FragmentAlbumsBinding
import com.example.appsfactory.features.lastfm.model.Album
import com.example.appsfactory.utils.setDisplayHomeAsUpEnabled
import com.example.appsfactory.utils.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel


class AlbumsListFragment : BaseFragment() {

    override val _viewModel: AlbumsListViewModel by viewModel()

    private lateinit var _binding: FragmentAlbumsBinding

    private val adapter = AlbumAdapter(
        clickListener = AlbumListener(
            { album ->
            navigateToAlbumDetails(album)
        }, saveListener = { album ->
            _viewModel.removeAlbum (album)
        }, checkListener = { true })
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_albums, container, false
            )

        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAndObservers()
    }

    private fun setupRecyclerAndObservers() {

        _binding.searchFab.setOnClickListener {
            navigateToSearchScreen()
        }

        _binding.albumsRecycler.layoutManager = LinearLayoutManager(this.context)
        _binding.albumsRecycler.adapter = adapter
        _viewModel.albumsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                _viewModel.invalidateShowNoData()
            }
        })
    }

    private fun navigateToAlbumDetails(album: Album) {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(album)
            )
        )
    }

    private fun navigateToSearchScreen() {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                AlbumsListFragmentDirections.actionAlbumsListFragmentToSearchFragment()
            )
        )
    }

}
