package com.stikubank.githubuserapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.stikubank.githubuserapp"
    const val SCHEME = "content"

    class favColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val COL_NAME = "name"
            const val COL_USERNAME = "username"
            const val COL_AVATAR = "avatar_url"
            const val COL_ISFAV = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}