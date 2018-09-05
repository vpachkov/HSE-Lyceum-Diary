package com.example.slava.projectkek.domain.utils

import android.content.res.Resources
import android.util.TypedValue



object PixelConverter {

    fun convertDpToPx(pix: Int, res: Resources): Int{
        val px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, pix.toFloat(), res.getDisplayMetrics()))
        return px
    }

}