package com.stikubank.githubuserapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stikubank.githubuserapp.R
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.ui.main.DetailUserActivity


class FavAdapter(private val activity: Activity) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    var userList = ArrayList<User>()

        set(userList){
            if (userList.size > 0){
                this.userList.clear()
            }
            this.userList.addAll(userList)
            print(this.userList.size)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return FavViewHolder(view)

    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        println(this.userList.size)
        return this.userList.size
    }



    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(user: User) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.findViewById(R.id.iv_user))
                val data = findViewById<TextView>(R.id.tv_username)
                data.text = user.login

                itemView.setOnClickListener {
                    println("Clicked")
                    print(user.login)
                    val intent = Intent(activity, DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                    }
                    activity.startActivity(intent)
            }
        }

    }}


}