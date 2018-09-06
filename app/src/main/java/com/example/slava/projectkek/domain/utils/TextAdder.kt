package com.example.slava.projectkek.domain.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.slava.projectkek.R
import org.json.JSONObject

object TextAdder {

    private var dateSize = 5f
    private var daySize = 5f
    private var lessonSize = 5f
    private var homeworkSize = 5f
    private var blockMargin = 5f
    private var grayLineWidth = 5f
    private var grayLineHeight = 5f


    fun addHomework(container: LinearLayout , subject: String, work: String, context: Context){

        lessonSize = PixelConverter.convertDpToPixels(7.5f, context).toFloat()
        homeworkSize = PixelConverter.convertDpToPixels(7f, context).toFloat()

        val subView = LinearLayout(context)

        val sub_view_subject = TextView(context)


        sub_view_subject.text = subject
        sub_view_subject.textSize = lessonSize
        sub_view_subject.setTypeface(null, Typeface.BOLD)
        sub_view_subject.setTextColor(Color.parseColor("#000000"))

        val sub_view_excercise = TextView(context)

        sub_view_excercise.text = work
        sub_view_excercise.textSize = homeworkSize
        subView.orientation = LinearLayout.VERTICAL
        subView.addView(sub_view_subject)
        subView.addView(sub_view_excercise)

        subView.setPadding(0 , 20 , 0 , 0)

        container.addView(subView)
    }

    fun addSchedule(container: LinearLayout , subjectName: String, context: Context){

        lessonSize = PixelConverter.convertDpToPixels(7.5f, context).toFloat()

        val subject = TextView(context)

        subject.text = subjectName
        subject.textSize = lessonSize
        subject.setTypeface(null, Typeface.BOLD)
        subject.setTextColor(Color.parseColor("#000000"))
        subject.setPadding(0 , 20 , 0 , 0)

        container.addView(subject)
    }

    fun addDiaryBlock(container: LinearLayout, context: Context, diary: JSONObject, homework: JSONObject, assessments: JSONObject){

        lessonSize = PixelConverter.convertDpToPixels(7.5f, context).toFloat()
        homeworkSize = PixelConverter.convertDpToPixels(7f, context).toFloat()
        dateSize = PixelConverter.convertDpToPixels(6.8f, context).toFloat()
        daySize = PixelConverter.convertDpToPixels(8f, context).toFloat()
        blockMargin = PixelConverter.convertDpToPixels(20f, context).toFloat()
        grayLineHeight = PixelConverter.convertDpToPixels(3.5f, context).toFloat()
        grayLineWidth = PixelConverter.convertDpToPixels(120f, context).toFloat()


        var isHomework = false
        var isAssessments = false

        //homeworkKeys.iterator().asSequence().contains()

        for (i in diary.keys()){
            var lastHomework = "1"
            val diaryBlock = LinearLayout(context)
            diaryBlock.orientation = LinearLayout.VERTICAL
            diaryBlock.setPadding(lessonSize.toInt(), lessonSize.toInt(), lessonSize.toInt(), lessonSize.toInt())
            diaryBlock.setBackgroundResource(R.drawable.rounded_item_light_gray)

            val diaryBlockParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (diary.keys().asSequence().last().toString() == i){
                diaryBlockParams.setMargins(0, blockMargin.toInt(),0,3* blockMargin.toInt())
            }
            else
                diaryBlockParams.setMargins(0, blockMargin.toInt(),0,0)
            diaryBlock.layoutParams = diaryBlockParams

            val dateNameView = TextView(context)
            dateNameView.text = parseDate(diary.getJSONObject(i).getString("name"))
            dateNameView.textSize  = dateSize

            val dayNameView = TextView(context)
            dayNameView.text = diary.getJSONObject(i).getString("title")
            dayNameView.textSize  = daySize
            dayNameView.setTypeface(null, Typeface.BOLD)
            dayNameView.setTextColor(Color.parseColor("#000000"))



            val grayLine = View(context)
            grayLine.layoutParams = ViewGroup.LayoutParams(grayLineWidth.toInt(), grayLineHeight.toInt())
            grayLine.setBackgroundResource(R.drawable.rounded_item_gray)

            diaryBlock.addView(dateNameView)
            diaryBlock.addView(dayNameView)
            diaryBlock.addView(grayLine)

            isHomework = homework.keys().asSequence().contains(i)
            isAssessments = assessments.keys().asSequence().contains(i)


            for (j in diary.getJSONObject(i).getJSONObject("items").keys()){
                val subjectNameView = TextView(context)
                subjectNameView.text = diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")
                subjectNameView.textSize = lessonSize
                subjectNameView.setTypeface(null, Typeface.BOLD)
                subjectNameView.setTextColor(Color.parseColor("#000000"))

                val subjectNameViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                subjectNameViewParams.setMargins(0, blockMargin.toInt(),0,0)

                subjectNameView.layoutParams = subjectNameViewParams

                diaryBlock.addView(subjectNameView)

                if (isAssessments && assessments.getJSONObject(i).getJSONObject("items").keys().asSequence().contains(j)){
                    for (mks in 0 until assessments.getJSONObject(i).getJSONObject("items")
                            .getJSONObject(j).getJSONArray("assessments").length()){
                        diaryBlock.addView(
                               makeMarkBlock(
                                       assessments.getJSONObject(i).getJSONObject("items")
                                               .getJSONObject(j).getJSONArray("assessments").getJSONObject(mks)
                                               .getString("value").toUpperCase(),
                                       context
                               )
                        )
                    }
                }

                if (isHomework){
                    if (homework.getJSONObject(i).getJSONObject("items").keys().asSequence()
                                    .contains(diary.getJSONObject(i).getJSONObject("items")
                                            .getJSONObject(j).getString("name")) && lastHomework != diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")){
                        val homeworkView = TextView(context)
                        homeworkView.text = homework.getJSONObject(i).getJSONObject("items").getJSONObject(diary.getJSONObject(i)
                                .getJSONObject("items").getJSONObject(j).getString("name")).getJSONObject("homework")
                                .getJSONObject("1").getString("value")
                        homeworkView.textSize = homeworkSize

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

    private fun makeMarkBlock(mark: String, context: Context): TextView{
        val markTextView = TextView(context)
        markTextView.text = mark
        markTextView.setTypeface(null, Typeface.BOLD)
        val markTextViewParams = RelativeLayout.LayoutParams(
                PixelConverter.convertDpToPixels(38f , context),
                PixelConverter.convertDpToPixels(22f , context)
        )
        markTextViewParams.setMargins(0, PixelConverter.convertDpToPixels(7.5f, context), 0, 0)
        markTextView.layoutParams = markTextViewParams
        markTextView.gravity = Gravity.CENTER
        markTextView.setTextColor(Color.parseColor("#ffffff"))
        markTextView.background = context.getResources().getDrawable(R.drawable.rounded_mark)
        return markTextView
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