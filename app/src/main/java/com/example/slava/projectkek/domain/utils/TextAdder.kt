package com.example.slava.projectkek.domain.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.slava.projectkek.R
import org.json.JSONObject
import org.w3c.dom.Text

object TextAdder {

    private var dateSize = 5f
    private var daySize = 5f
    private var lessonSize = 5f
    private var homeworkSize = 5f
    private var blockMargin = 5f
    private var grayLineWidth = 5f
    private var grayLineHeight = 5f


    fun addHomework(container: LinearLayout , subject: String, work: String, context: Context){

        lessonSize = PixelConverter.convertDpToPixel(context, 20f)
        homeworkSize = PixelConverter.convertDpToPixels(7f, context).toFloat()

        val subView = LinearLayout(context)

        val sub_view_subject = TextView(context)


        sub_view_subject.text = subject
        sub_view_subject.setTextSize(TypedValue.COMPLEX_UNIT_SP , 19f)
        sub_view_subject.setTypeface(null, Typeface.BOLD)
        sub_view_subject.setTextColor(Color.parseColor("#000000"))

        val sub_view_excercise = TextView(context)

        sub_view_excercise.text = work
        sub_view_excercise.setTextSize(TypedValue.COMPLEX_UNIT_SP , 16f)
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
        subject.setTextSize(TypedValue.COMPLEX_UNIT_SP , 19f)
        subject.setTypeface(null, Typeface.BOLD)
        subject.setTextColor(Color.parseColor("#000000"))
        subject.setPadding(0 , 20 , 0 , 0)

        container.addView(subject)
    }

    fun addDiaryBlock(container: LinearLayout, context: Context, diary: JSONObject){

        lessonSize = PixelConverter.convertDpToPixels(7.5f, context).toFloat()
        homeworkSize = PixelConverter.convertDpToPixels(7f, context).toFloat()
        dateSize = PixelConverter.convertDpToPixels(6.8f, context).toFloat()
        daySize = PixelConverter.convertDpToPixels(8f, context).toFloat()
        blockMargin = PixelConverter.convertDpToPixels(20f, context).toFloat()
        grayLineHeight = PixelConverter.convertDpToPixels(3.5f, context).toFloat()
        grayLineWidth = PixelConverter.convertDpToPixels(120f, context).toFloat()


        for (i in diary.keys()){
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
            dateNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 15f)

            val dayNameView = TextView(context)
            dayNameView.text = diary.getJSONObject(i).getString("title")
            dayNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 20f)
            dayNameView.setTypeface(null, Typeface.BOLD)
            dayNameView.setTextColor(Color.parseColor("#000000"))



            val grayLine = View(context)
            grayLine.layoutParams = ViewGroup.LayoutParams(grayLineWidth.toInt(), grayLineHeight.toInt())
            grayLine.setBackgroundResource(R.drawable.rounded_item_gray)

            diaryBlock.addView(dateNameView)
            diaryBlock.addView(dayNameView)
            diaryBlock.addView(grayLine)

            for (j in diary.getJSONObject(i).getJSONObject("items").keys()){
                val subjectNameView = TextView(context)
                subjectNameView.text = diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("name")
                subjectNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 19f)
                subjectNameView.setTypeface(null, Typeface.BOLD)
                subjectNameView.setTextColor(Color.parseColor("#000000"))

                val subjectNameViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                subjectNameViewParams.setMargins(0, blockMargin.toInt(),0,0)

                subjectNameView.layoutParams = subjectNameViewParams

                diaryBlock.addView(subjectNameView)

                val extraInformationBlock = makeExtraInformationBlock(context)

                if (diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).keys().asSequence().contains("assessments")){
                    for (mks in 0 until diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getJSONArray("assessments").length())
                        extraInformationBlock.addView(
                                makeMarkBlock(
                                        diary.getJSONObject(i).getJSONObject("items")
                                                .getJSONObject(j).getJSONArray("assessments").getJSONObject(mks)
                                                .getString("value").toUpperCase(),
                                        context
                                )
                        )
                }
                extraInformationBlock.addView(
                        makeStartTimeBlock(diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("starttime"), context)
                )
                extraInformationBlock.addView(
                        makeTeacherBlock(diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getString("teacher"), context)
                )

                diaryBlock.addView(extraInformationBlock)

                if (diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).keys().asSequence().contains("homework")){
                    for (hw in diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getJSONObject("homework").keys())
                        diaryBlock.addView(
                                makeHomeworkBlock(diary.getJSONObject(i).getJSONObject("items").getJSONObject(j).getJSONObject("homework").getJSONObject(hw).getString("value") , context)
                        )
                }

            }

            container.addView(diaryBlock)

        }





    }

    private fun makeHomeworkBlock(homework: String, context: Context): TextView{
        val homeworkView = TextView(context)
        homeworkView.text = homework
        homeworkView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 16f)

        val homeworkViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        homeworkViewParams.setMargins(0,
                PixelConverter.convertDpToPixels(5f , context),0,0)

        homeworkView.layoutParams = homeworkViewParams

        return homeworkView
    }

    private fun makeMarkBlock(mark: String, context: Context): TextView{
        val markTextView = TextView(context)
        markTextView.text = mark
        markTextView.setTypeface(null, Typeface.BOLD)
        val markTextViewParams = RelativeLayout.LayoutParams(
                PixelConverter.convertDpToPixels(38f , context),
                PixelConverter.convertDpToPixels(22f , context)
        )
        markTextViewParams.setMargins(0, 0, PixelConverter.convertDpToPixels(7.5f, context), 0)
        markTextView.layoutParams = markTextViewParams
        markTextView.gravity = Gravity.CENTER
        markTextView.setTextColor(Color.parseColor("#ffffff"))
        markTextView.background = context.getResources().getDrawable(R.drawable.rounded_mark)
        return markTextView
    }

    private fun makeExtraInformationBlock(context: Context): LinearLayout{
        val extraInformationBlock = LinearLayout(context)
        extraInformationBlock.orientation = LinearLayout.HORIZONTAL
        val extraInformationBlockParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT)
        extraInformationBlockParams.setMargins(0, PixelConverter.convertDpToPixels(7.5f, context), 0, 0)
        extraInformationBlock.layoutParams = extraInformationBlockParams
        return extraInformationBlock
    }

    private fun makeStartTimeBlock(startTime:String, context: Context): TextView{
        val parsedStartTime = startTime[0].toString() + startTime[1]+startTime[2]+startTime[3]+startTime[4]
        val startTimeView = TextView(context)
        startTimeView.text = parsedStartTime
        val startTimeViewParams = RelativeLayout.LayoutParams(
                PixelConverter.convertDpToPixels(48f , context),
                PixelConverter.convertDpToPixels(22f , context)
        )
        startTimeViewParams.setMargins(0, 0, PixelConverter.convertDpToPixels(7.5f, context), 0)
        startTimeView.gravity = Gravity.CENTER
        startTimeView.layoutParams = startTimeViewParams
        startTimeView.background = context.getResources().getDrawable(R.drawable.rounded_lesson_start_time)
        startTimeView.setTypeface(null, Typeface.BOLD)
        startTimeView.setTextColor(Color.parseColor("#000000"))
        return startTimeView
    }

    private fun makeTeacherBlock(teacher: String, context: Context): TextView{
        val teacherList = teacher.split(' ')
        var parsedTeacher = teacherList[0]
        if (1 < teacherList.size  && teacherList[1].isNotEmpty())
            parsedTeacher += " " + teacherList[1][0] + "."

        if (2 < teacherList.size && teacherList[2].isNotEmpty())
            parsedTeacher += teacherList[2][0] + "."
        val teacherView = TextView(context)
        teacherView.text = parsedTeacher
        val teacherViewParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                PixelConverter.convertDpToPixels(22f , context)
        )
        teacherView.setPadding(
                PixelConverter.convertDpToPixels(8f , context),
                0,
                PixelConverter.convertDpToPixels(8f , context),
                0
        )
        teacherViewParams.setMargins(0, 0, PixelConverter.convertDpToPixels(7.5f, context), 0)
        teacherView.gravity = Gravity.CENTER
        teacherView.layoutParams = teacherViewParams
        //startTimeView.setTextColor(Color.parseColor("#ffffff"))
        teacherView.background = context.getResources().getDrawable(R.drawable.rounded_lesson_start_time)
        teacherView.setTypeface(null, Typeface.BOLD)
        teacherView.setTextColor(Color.parseColor("#000000"))
        return teacherView
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