package com.example.slava.projectkek.data.model.token

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Result {
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("expires")
    @Expose
    var expires: String? = null
}