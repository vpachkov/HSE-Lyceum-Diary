package com.example.slava.projectkek.domain.utils

import android.util.Log
import com.example.slava.projectkek.data.model.getschedule.Schedule
import com.example.slava.projectkek.data.model.token.Token
import com.google.gson.Gson
import khttp.get


object EljurRequester {
    private val ELJUR_ADRESS = "https://api.eljur.ru/api/"
    private val DEVKEY = "8227490faaaa60bb94b7cb2f92eb08a4"
    private val VENDOR = "hselyceum"
    private val OUT_FORMAT = "json"

    fun request(method:String, eljurId: String, date:String): Schedule {
        Log.e("keek", "rofler")
        var mainUrl = ELJUR_ADRESS + method
        var params = hashMapOf(
                "devkey" to DEVKEY,
                "auth_token" to eljurId,
                "days" to date,
                "vendor" to VENDOR,
                "out_format" to OUT_FORMAT
        )
        lateinit var obj: Schedule
        Log.e("keek", "rofler")
        val lol = get(mainUrl, data = params)
        obj = Gson().fromJson(lol.text, Schedule::class.java)
        Log.e("keek", "rofler")
        return obj

    }
}