package com.example.slava.projectkek.domain.utils

import android.util.Log
import com.example.slava.projectkek.data.model.getschedule.Schedule
import com.example.slava.projectkek.data.model.token.Token
import com.google.gson.Gson
import khttp.get
import org.json.JSONObject


object ElJurApiHelper {

    fun calculateLessonEndTime(scheduleObj: JSONObject): String{
        val endTime  = scheduleObj.getJSONObject(scheduleObj.keys().asSequence().last().toString()).getString("endtime")
        val parsedEndTime = endTime[0].toString()+endTime[1]+endTime[3]+endTime[4]
        return parsedEndTime
    }
}