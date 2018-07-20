package com.example.slava.projectkek.data.model.getschedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("students")
    @Expose
    var students: Students? = null

}