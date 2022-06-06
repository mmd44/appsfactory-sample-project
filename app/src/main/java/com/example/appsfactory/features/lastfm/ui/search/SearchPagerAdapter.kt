package com.example.appsfactory.features.lastfm.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appsfactory.R
import com.example.appsfactory.databinding.ItArtistBinding
import com.example.appsfactory.features.lastfm.model.Artist


class SearchPagerAdapter(private val clickListener: ArtistListener) :
    PagingDataAdapter<Artist, SearchPagerAdapter.SearchViewHolder>(SearchDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder.from(parent)

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val artist = getItem(position)
        artist?.let { holder.bind(clickListener, it) }
    }

    class SearchViewHolder private constructor(val binding: ItArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ArtistListener, item: Artist) {
            binding.item = item
            val options = RequestOptions()
                .placeholder(R.drawable.ic_music)
            Glide.with(this.itemView.context).load(item.getMediumImageUrl())
                .apply(options)
                .into(binding.artistImage)
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItArtistBinding.inflate(layoutInflater, parent, false)
                return SearchViewHolder(binding)
            }
        }
    }

    object SearchDiffUtil : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
}

class ArtistListener(val clickListener: (artistName: String) -> Unit) {
    fun onClick(artistName: String) {
        clickListener(artistName)
    }
}