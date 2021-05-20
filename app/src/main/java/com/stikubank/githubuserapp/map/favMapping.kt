package com.stikubank.githubuserapp.map

import android.database.Cursor
import android.provider.ContactsContract
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.db.DatabaseContract

object favMapping {

    fun mapCursorToArray(favCursor: Cursor?): ArrayList<User>{
        val favList = ArrayList<User>()

        favCursor?.apply {
            while (moveToNext()){
                val name = getString(getColumnIndexOrThrow(DatabaseContract.favColumns.COL_NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.favColumns.COL_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.favColumns.COL_AVATAR))
                val isfav = getString(getColumnIndexOrThrow(DatabaseContract.favColumns.COL_ISFAV))
                favList.add(User(name, username,avatar,isfav))
            }
        }
        return favList
    }
}