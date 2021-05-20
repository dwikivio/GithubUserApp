package com.stikubank.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.stikubank.githubuserapp.db.DatabaseContract.AUTHORITY
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.CONTENT_URI
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.TABLE_NAME
import com.stikubank.githubuserapp.db.favHelper

class FavProvider : ContentProvider() {

    companion object{
        private const val myFAV = 1
        private const val myFAV_ID = 2
        private val mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var mFavHelper: favHelper

        init {
            mUriMatcher.addURI(AUTHORITY, TABLE_NAME, myFAV)
            mUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", myFAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        mFavHelper = favHelper.getInstance(context as Context)
        mFavHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (mUriMatcher.match(uri)) {
            myFAV-> mFavHelper.queryAll()
            myFAV_ID -> mFavHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insert: Long = when (myFAV) {
            mUriMatcher.match(uri) -> mFavHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$insert")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val alter: Int = when (myFAV_ID) {
            mUriMatcher.match(uri) -> mFavHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return alter
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val drop: Int = when (myFAV_ID) {
            mUriMatcher.match(uri) -> mFavHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return drop
    }
}