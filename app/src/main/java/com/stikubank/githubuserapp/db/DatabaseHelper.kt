package com.stikubank.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private const val DB_NAME = "dbuser"
        private const val DB_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.favColumns.COL_NAME} TEXT NOT NULL, "+
                "${DatabaseContract.favColumns.COL_USERNAME} TEXT NOT NULL, "+
                "${DatabaseContract.favColumns.COL_AVATAR} TEXT NOT NULL, "+
                "${DatabaseContract.favColumns.COL_ISFAV} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}