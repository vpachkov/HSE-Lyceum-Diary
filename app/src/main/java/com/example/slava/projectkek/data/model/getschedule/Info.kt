package com.example.slava.projectkek.data.model.getschedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Info {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("lessons")
    @Expose
    var lessons: List<Lesson>? = null

}