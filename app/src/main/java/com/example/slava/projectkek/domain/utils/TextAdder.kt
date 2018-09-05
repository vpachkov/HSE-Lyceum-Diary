package com.example.slava.projectkek.domain.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.eclipsesource.json.JsonObject
import com.example.slava.projectkek.R
import com.example.slava.projectkek.R.id.header_text
import okhttp3.Response
import org.json.JSONObject

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

    fun addDiaryBlock(container: LinearLayout, context: Context, diary: JSONObject, homework: JSONObject){

        val homeworkKeys = homework.keys()
        var isHomework = false

        //homeworkKeys.iterator().asSequence().contains()

        for (i in diary.keys()){
            var lastHomework = "1"
            val diaryBlock = LinearLayout(context)
            diaryBlock.orientation = LinearLayout.VERTICAL
            diaryBlock.setPadding(20,20,20,20)
            diaryBlock.setBackgroundResource(R.drawable.rounded_item_light_gray)

            val diaryBlockParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (diary.keys().asSequence().last().toString() == i){
                diaryBlockParams.setMargins(0,30,0,120)
            }
            else
                diaryBlockParams.setMargins(0,30,0,0)
            diaryBlock.layoutParams = diaryBlockParams

            val dateNameView = TextView(context)
            dateNameView.text = parseDate(diary.getJSONObject(i).getString("name"))
            dateNameView.textSize  = 18f

            val dayNameView = TextView(context)
            dayNameView.text = diary.getJSONObject(i).getString("title")
            dayNameView.textSize  = 23f
            dayNameView.setTypeface(null, Typeface.BOLD)
            dayNameView.setTextColor(Color.parseColor("#000000"))



            val grayLine = View(context)
            grayLine.layoutParams = ViewGroup.LayoutParams(380, 11)
            grayLine.setBackgroundResource(R.drawable.rounded_item_gray)

            diaryBlock.addView(dateNameView)
            diaryBlock.addView(dayNameView)
            diaryBlock.addView(grayLine)

            isHomework = homework.keys().asSequence().contains(i)


            for (j in diary.getJSONObject(i).getJSONObject("items").keys()){
                val subjectNameView = TextView(context)
                subjectNameView.text = diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")
                subjectNameView.textSize = 20f
                subjectNameView.setTypeface(null, Typeface.BOLD)
                subjectNameView.setTextColor(Color.parseColor("#000000"))

                val subjectNameViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                subjectNameViewParams.setMargins(0,30,0,0)

                subjectNameView.layoutParams = subjectNameViewParams

                diaryBlock.addView(subjectNameView)

                if (isHomework){
                    if (homework.getJSONObject(i).getJSONObject("items").keys().asSequence()
                                    .contains(diary.getJSONObject(i).getJSONObject("items")
                                            .getJSONObject(j).getString("name")) && lastHomework != diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")){
                        val homeworkView = TextView(context)
                        homeworkView.text = homework.getJSONObject(i).getJSONObject("items").getJSONObject(diary.getJSONObject(i)
                                .getJSONObject("items").getJSONObject(j).getString("name")).getJSONObject("homework")
                                .getJSONObject("1").getString("value")
                        homeworkView.textSize = 20f

                        val homeworkViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        //homeworkViewParams.setMargins(0,15,0,0)

                        homeworkView.layoutParams = homeworkViewParams

                        diaryBlock.addView(homeworkView)

                        lastHomework = diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")


                    }
                }
            }

            container.addView(diaryBlock)

        }





    }

    private fun parseDate(date :String): String{
        val month: String = date[4].toString()+date[5].toString() + " "

        var parsedDate = if (date[6].toString() == "0")
            date[7].toString()
        else
            date[6].toString()+date[7].toString()

        parsedDate += " "


        parsedDate += when(month){
            "01" -> "яннваря"
            "02" -> "февраля"
            "03" -> "марта"
            "04" -> "апреля"
            "05" -> "мая"
            "06" -> "июня"
            "07" -> "июля"
            "08" -> "августа"
            "09" -> "сентября"
            "10" -> "октября"
            "11" -> "ноября"
            else -> "декабря"
        }
        return parsedDate
    }


}