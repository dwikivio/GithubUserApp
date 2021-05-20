package com.stikubank.githubuserapp.ui.main

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stikubank.githubuserapp.R
import com.stikubank.githubuserapp.adapter.FavAdapter
import com.stikubank.githubuserapp.adapter.SectionPagerAdapter
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.databinding.ActivityDetailUserBinding
import com.stikubank.githubuserapp.db.favHelper

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        println(username)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        username?.let { viewModel.setUserDetail(it) }
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.apply {
                    tvUsername.text = it.login
                    tvName.text = "${it.name}"
                    tvRepo.text = it.public_repos.toString()
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvLocation.text = "${it.location}"
                    tvBio.text = "${it.bio}"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .centerCrop()
                        .into(ivProfile)
                }
            }
        })

        var fab: View = findViewById<FloatingActionButton>(R.id.toggle_fav)

        var statusFav = false


        fun setStatusFav() {
            viewModel.getUserDetail().observe(this, {
                val mfavHelper = favHelper(this)
                if (it != null) {
                    val dataUser:User? = mfavHelper.usernameQuery(it.login)
//                    print(dataUser.login)
                    if (dataUser == null) {
                        fab.setBackgroundResource(R.drawable.ic_fav_enable)
                        val values = ContentValues()

                        values.put("name", it.name ?: "")
                        values.put("username", it.login)
                        values.put("avatar_url", it.avatar_url)
                        values.put("isFav", statusFav)

                        print(values)


                        mfavHelper.insert(values)

                        print("cari data yang di DB")
                        println(mfavHelper.queryById(it.login).count)

                        var favAdapter: FavAdapter = FavAdapter(this)
                        favAdapter.userList = mfavHelper.userQuery()!!

                        Toast.makeText(
                            this,
                            "Success Adding $username to Favorite",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        fab.setBackgroundResource(R.drawable.ic_fav_disable)

//                        val mfavHelper = favHelper(this)
                        mfavHelper.deleteById(it.login)

                        Toast.makeText(this, "Remove $username from Favorite", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }

        fab.setOnClickListener {
            setStatusFav()

        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }
}