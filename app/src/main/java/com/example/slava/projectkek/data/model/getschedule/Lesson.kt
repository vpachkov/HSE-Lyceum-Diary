package com.example.slava.projectkek.data.model.getschedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Lesson {

    @SerializedName("average")
    @Expose
    var average: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("marks")
    @Expose
    var marks: List<Mark>? = null

}