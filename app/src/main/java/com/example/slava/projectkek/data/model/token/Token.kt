package com.example.slava.projectkek.data.model.token

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {
    @SerializedName("response")
    @Expose
    var response: Response? = null
}