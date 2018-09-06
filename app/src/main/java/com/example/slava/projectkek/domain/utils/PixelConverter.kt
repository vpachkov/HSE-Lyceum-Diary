package com.example.slava.projectkek.domain.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue



object PixelConverter {

    fun convertDpToPx(dp: Int, res: Resources): Int{
        val px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), res.getDisplayMetrics()))
        return px
    }

    fun convertDpToPixels(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()).toInt()
    }

}