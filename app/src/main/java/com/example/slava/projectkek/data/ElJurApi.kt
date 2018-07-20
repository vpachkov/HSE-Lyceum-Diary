package com.example.slava.projectkek.data

import com.example.slava.projectkek.data.model.token.Token
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//cf1c071b751048938ca1bd0f586a22f2

//https://newsapi.org/v2/top-headlines?country=us&apiKey=cf1c071b751048938ca1bd0f586a22f2

interface ElJurApi {
    @GET("auth")
    fun getToken(@Query(value = "login") login: String,
                 @Query("password") password: String,
                 @Query("vendor") vendor: String,
                 @Query("devkey") devkey: String): Call<Token>

}