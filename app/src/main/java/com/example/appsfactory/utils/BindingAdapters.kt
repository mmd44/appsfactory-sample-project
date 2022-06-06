package com.example.appsfactory.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.appsfactory.R
import com.example.appsfactory.features.lastfm.model.Album


object BindingAdapters {

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.visibility = View.GONE
            }
        }
    }


    @BindingAdapter("android:saveAlbum")
    @JvmStatic
    fun bindBookmarkSelection(image: ImageView, isSaved: Boolean? = true) {
        val context = image.context
        if (isSaved == true) {
            image.setImageResource(R.drawable.ic_bookmark_fill)
            image.contentDescription = context.getString(R.string.saved)
        } else {
            image.setImageResource(R.drawable.ic_bookmark)
            image.contentDescription = context.getString(R.string.not_saved)
        }
    }

    @BindingAdapter("android:albumImage")
    @JvmStatic
    fun bindAlbumImage(image: ImageView, album: Album) {
        val context = image.context
        if (album.imageBitmap != null) {
            image.setImageBitmap(album.imageBitmap)
        }
    }
}