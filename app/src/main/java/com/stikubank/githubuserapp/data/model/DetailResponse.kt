package com.stikubank.githubuserapp.data.model

data class DetailResponse(
        val login: String,
        val id: Int,
        val name: String,
        val avatar_url: String,
        val followers_url: String,
        val following_url: String,
        val location: String,
        val bio: String,
        val public_repos: Int,
        val followers: Int,
        val following: Int
)
