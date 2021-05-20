package com.stikubank.githubuserapp.api

import com.stikubank.githubuserapp.data.model.DetailResponse
import com.stikubank.githubuserapp.data.model.User
import com.stikubank.githubuserapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 37c3fc18ad94ce7aca4c022b3bd6d944b565435a")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{uname}")
    @Headers("Authorization: token 37c3fc18ad94ce7aca4c022b3bd6d944b565435a")
    fun getDetailUser(
        @Path("uname") uname: String
    ): Call<DetailResponse>

    @GET("users/{uname}/followers")
    @Headers("Authorization: token 37c3fc18ad94ce7aca4c022b3bd6d944b565435a")
    fun getFollowers(
        @Path("uname") uname: String
    ): Call<ArrayList<User>>

    @GET("users/{uname}/following")
    @Headers("Authorization: token 37c3fc18ad94ce7aca4c022b3bd6d944b565435a")
    fun getFollowing(
        @Path("uname") uname: String
    ): Call<ArrayList<User>>
}