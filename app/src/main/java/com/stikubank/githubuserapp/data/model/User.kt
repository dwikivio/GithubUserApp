package com.stikubank.githubuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        val login: String,
        var id: String,
        val avatar_url: String,
        val isFav: String
): Parcelable