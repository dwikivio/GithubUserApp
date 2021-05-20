package com.stikubank.githubuserapp.ui.main

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.stikubank.githubuserapp.R
import com.stikubank.githubuserapp.R.layout
import com.stikubank.githubuserapp.adapter.FavAdapter
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.databinding.ActivityFavoriteBinding
import com.stikubank.githubuserapp.db.DatabaseContract.favColumns.Companion.CONTENT_URI
import com.stikubank.githubuserapp.db.favHelper
import com.stikubank.githubuserapp.map.favMapping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favAdapter: FavAdapter



    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mfavHelper = favHelper(this)
        setContentView(layout.activity_favorite)

        supportActionBar?.title = "User Favorit"

        val recyclerView = findViewById<RecyclerView>(R.id.rv_fav)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        favAdapter = FavAdapter(this)
        favAdapter.userList = mfavHelper.userQuery()!!
        recyclerView.adapter = favAdapter


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean) {
                loadNoteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadNoteAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if(list != null){
                favAdapter.userList = list
            }
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }


    private fun loadNoteAsync(){
        GlobalScope.launch (Dispatchers.Main){
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                favMapping.mapCursorToArray(cursor)
            }
            val favData = deferredNotes.await()
            if (favData.size > 0) {
                favAdapter.userList = favData
            } else {
                favAdapter.userList = ArrayList()
                showSnackbarMessage()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mfavHelper = favHelper(this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_fav)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        favAdapter = FavAdapter(this)
        favAdapter.userList = mfavHelper.userQuery()!!
        recyclerView.adapter = favAdapter
    }

    private fun showSnackbarMessage() {
        Snackbar.make(binding.rvFav, "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
    }
}



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val mfavHelper = favHelper(this)
//        val data: ArrayList<User>? = mfavHelper.userQuery()
//        if (data != null) {
//            for (i in data){
//                print(i.login)
//            }
//        }
////        println(data.getString(1))
//
//
//
//        binding = ActivityFavoriteBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        favAdapter = FavAdapter()
//        favAdapter.notifyDataSetChanged()
//
//
//
//        binding.apply {
//            rvFav.layoutManager = LinearLayoutManager(this@FavoriteActivity)
//            rvFav.setHasFixedSize(true)
//            favAdapter = FavAdapter()
//            rvFav.adapter = favAdapter
//        }
//
//        favAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
//            override fun onItemClicked(data: User) {
//                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
//                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
//                    startActivity(it)
//                }
//            }
//        })
//
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        print("Data Query")
//        outState.putParcelableArrayList(EXTRA_STATE, favAdapter.listFav)
//    }
