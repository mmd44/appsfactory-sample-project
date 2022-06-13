package com.example.appsfactory.features.lastfm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Track(
    val name: String
) : Parcelable