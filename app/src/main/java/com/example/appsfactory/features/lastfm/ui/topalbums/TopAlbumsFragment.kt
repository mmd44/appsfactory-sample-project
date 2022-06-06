package com.example.appsfactory.features.lastfm.ui.topalbums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseFragment
import com.example.appsfactory.databinding.FragmentTopAlbumsBinding
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumAdapter
import com.example.appsfactory.features.lastfm.ui.savedalbums.AlbumListener
import com.example.appsfactory.utils.setDisplayHomeAsUpEnabled
import com.example.appsfactory.utils.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TopAlbumsFragment : BaseFragment() {

    private val args: TopAlbumsFragmentArgs by navArgs()

    override val _viewModel: TopAlbumsViewModel by viewModel {
        parametersOf(args.artistName)
    }

    private lateinit var _binding: FragmentTopAlbumsBinding

    private val adapter = AlbumAdapter(AlbumListener({}, {
        _viewModel.saveUnsaveAlbum(it)
    }, checkListener = { album ->
         _viewModel.checkIfSaved(album)
    }))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_top_albums, container, false
            )
        _binding.lifecycleOwner = this
        _binding.viewModel = _viewModel

        setDisplayHomeAsUpEnabled(true)
        setTitle(getString(R.string.top_albums))

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAndObservers()
        _viewModel.loadTopAlbums()
    }

    private fun setupRecyclerAndObservers() {

        _binding.topAlbumsRecycler.layoutManager = LinearLayoutManager(this.context)
        _binding.topAlbumsRecycler.adapter = adapter

        _viewModel.topAlbumsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        _viewModel.saveStatus.observe(viewLifecycleOwner, Observer {
            adapter.updateSaveStatus(it)
        })
    }
}