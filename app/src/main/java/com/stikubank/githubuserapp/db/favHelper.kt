package com.stikubank.githubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.COL_USERNAME
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.TABLE_NAME
import java.sql.SQLException

class favHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = dataBaseHelper.writableDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: favHelper? = null
        fun getInstance(context: Context): favHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: favHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COL_USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$COL_USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COL_USERNAME = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COL_USERNAME = '$id'", null)
    }

    fun userQuery(): ArrayList<User>? {
        val userList:ArrayList<User> = ArrayList()
        val res = database.rawQuery("select * from " + DATABASE_TABLE, null )
        while (res.moveToNext()){
            print(res.getString(0))
            var user:User = User(res.getString(1), "0", res.getString(2), res.getString(3))
            userList.add(user)
        }
        return userList
    }

    fun usernameQuery(username:String): User? {
        println("Executed")
        val mCount:Cursor = database.rawQuery("select count(*) from $DATABASE_TABLE", null)

        println("Mcount top = ${mCount.count}")
        return try {
            if (mCount.moveToFirst()){
                val res = database.rawQuery("select * from $DATABASE_TABLE where $COL_USERNAME = ? limit ?", arrayOf(username, "1"))
    //            println(res.getInt(0))
//                    println("printing res ${res.getString(0)}")
                if (res.moveToNext()){
                    User(res.getString(1), "0", res.getString(2), res.getString(3))
                }else{
                    println(res.moveToNext())
                    println(res.columnNames)
                    null
                }
            }else{
                println("Mcount else = ${mCount.count}")
                null
            }
        }catch (e : Exception){
            println("Mcount cathc = ${mCount.count}")
            println("Error $e")
            null
        }
    }


}