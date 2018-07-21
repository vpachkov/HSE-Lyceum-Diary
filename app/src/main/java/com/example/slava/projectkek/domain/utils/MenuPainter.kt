package com.example.slava.projectkek.domain.utils

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.slava.projectkek.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.childrenSequence
import org.w3c.dom.Text


object MenuPainter {
    fun paintMenu(menu: LinearLayout, currentItem: TextView) {

        for (item in menu.childrenSequence()){
            (item as TextView).setBackgroundResource(R.drawable.ripple_menu_item)
        }

        currentItem.setTypeface(null , Typeface.BOLD)
        currentItem.setBackgroundResource(R.drawable.gradient_menu_item_rounded)
        currentItem.setTextColor(Color.parseColor("#ffffff"))


    }

}