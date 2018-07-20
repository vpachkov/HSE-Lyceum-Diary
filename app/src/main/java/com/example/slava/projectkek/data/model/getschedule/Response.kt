package com.example.slava.projectkek.data.model.getschedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {

    @SerializedName("state")
    @Expose
    var state: Int? = null
    @SerializedName("error")
    @Expose
    var error: Any? = null
    @SerializedName("result")
    @Expose
    var result: Result? = null

}