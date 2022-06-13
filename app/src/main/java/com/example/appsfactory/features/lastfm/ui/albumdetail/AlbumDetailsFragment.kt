package com.example.appsfactory.features.lastfm.ui.albumdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsfactory.R
import com.example.appsfactory.base.BaseFragment
import com.example.appsfactory.databinding.FragmentAlbumDetailsBinding
import com.example.appsfactory.utils.setDisplayHomeAsUpEnabled
import com.example.appsfactory.utils.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AlbumDetailsFragment : BaseFragment() {

    private val args: AlbumDetailsFragmentArgs by navArgs()

    override val _viewModel: AlbumDetailsViewModel by viewModel {
        parametersOf(args.album)
    }

    private lateinit var _binding: FragmentAlbumDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_album_details, container, false
            )

        _binding.lifecycleOwner = this
        _binding.item = args.album

        setDisplayHomeAsUpEnabled(true)
        setTitle(getString(R.string.album_details))

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAndObservers()
    }

    private fun setupRecyclerAndObservers() {
        val adapter = TracksAdapter()
        _binding.tracksRecycler.layoutManager = LinearLayoutManager(this.context)
        _binding.tracksRecycler.adapter = adapter
        adapter.submitItemsList(args.album.tracks)
    }
}