package com.example.slava.projectkek.domain

import com.example.slava.projectkek.data.ElJurApi
import com.example.slava.projectkek.data.model.token.Token
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {
    private const val BASE_URL = "https://api.eljur.ru/api/"

    private var retrofit: Retrofit? = null

    private fun getAPI(): ElJurApi {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit!!.create<ElJurApi>(ElJurApi::class.java)
    }

    fun getToken(login: String, password: String , vendor: String = "hselyceum" , devkey:String = "8227490faaaa60bb94b7cb2f92eb08a4"): Call<Token> {
        return ApiHelper.getAPI().getToken(login, password, devkey, vendor)
    }
}