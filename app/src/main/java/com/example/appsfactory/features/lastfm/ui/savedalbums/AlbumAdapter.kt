package com.example.appsfactory.features.lastfm.ui.savedalbums

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.appsfactory.R
import com.example.appsfactory.databinding.ItAlbumBinding
import com.example.appsfactory.features.lastfm.model.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_VIEW_TYPE_ITEM = 1

class AlbumAdapter(val clickListener: AlbumListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var albumsList: MutableList<Album> = mutableListOf()

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemCount(): Int = albumsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Album>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                albumsList = list?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val albumItem = albumsList[position]
                holder.bind(clickListener, albumItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    fun updateSaveStatus(album: Album) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                val index = albumsList.indexOf(albumsList.find { it.name == album.name })
                notifyItemChanged(index)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_ITEM
    }

    class ViewHolder private constructor(val binding: ItAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(albumListener: AlbumListener, item: Album) {
            if (item.imageBitmap != null) {
                binding.albumImage.setImageBitmap(item.imageBitmap)
            } else {
                loadImageURL(this.itemView, item)
            }

            binding.item = item
            binding.clickListener = albumListener
            binding.executePendingBindings()
        }

        private fun loadImageURL(itemView: View, album: Album) {
            val options = RequestOptions()
                .placeholder(R.drawable.ic_music)
            Glide.with(itemView)
                .asBitmap()
                .load(album.getMediumImageUrl())
                .apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        album.imageBitmap = resource
                        return false
                    }
                }).into(binding.albumImage)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItAlbumBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class AlbumListener(
    val clickListener: (album: Album) -> Unit,
    val saveListener: (album: Album) -> Unit,
    val checkListener: (album: Album) -> Boolean
) {
    fun onClick(album: Album) {
        clickListener(album)
    }

    fun onSave(album: Album) {
        saveListener(album)
    }

    fun onCheckIfSaved(album: Album): Boolean {
        return checkListener(album)
    }
}
