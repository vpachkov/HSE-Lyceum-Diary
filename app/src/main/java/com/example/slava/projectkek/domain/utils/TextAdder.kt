package com.example.slava.projectkek.domain.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView

object TextAdder {
    fun addHomework(container: LinearLayout , subject: String, work: String, context: Context){

        val subView = LinearLayout(context)

        val sub_view_subject = TextView(context)


        sub_view_subject.text = subject
        sub_view_subject.textSize = 20f
        sub_view_subject.setTypeface(null, Typeface.BOLD)
        sub_view_subject.setTextColor(Color.parseColor("#000000"))

        val sub_view_excercise = TextView(context)

        sub_view_excercise.text = work
        sub_view_excercise.textSize = 20f
        subView.orientation = LinearLayout.VERTICAL
        subView.addView(sub_view_subject)
        subView.addView(sub_view_excercise)

        subView.setPadding(0 , 20 , 0 , 0)

        container.addView(subView)
    }

    fun addSchedule(container: LinearLayout , subjectName: String, context: Context){
        val subject = TextView(context)

        subject.text = subjectName
        subject.textSize = 20f
        subject.setTypeface(null, Typeface.BOLD)
        subject.setTextColor(Color.parseColor("#000000"))
        subject.setPadding(0 , 20 , 0 , 0)

        container.addView(subject)
    }

}