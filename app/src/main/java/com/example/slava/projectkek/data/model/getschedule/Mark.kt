package com.example.slava.projectkek.data.model.getschedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Mark {

    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("countas")
    @Expose
    var countas: String? = null
    @SerializedName("count")
    @Expose
    var count: Boolean? = null
    @SerializedName("comment")
    @Expose
    var comment: String? = null
    @SerializedName("lesson_comment")
    @Expose
    var lessonComment: Any? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("weight")
    @Expose
    var weight: String? = null

}

