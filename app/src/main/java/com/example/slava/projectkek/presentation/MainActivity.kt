package com.example.slava.projectkek.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.*
import khttp.get
import khttp.responses.Response
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.black_screen.*
import org.jetbrains.anko.doAsync
import kotlinx.android.synthetic.main.pop_up_menu.*
import kotlinx.android.synthetic.main.tomorrow_homework.*
import kotlinx.android.synthetic.main.tomorrow_schedule.*
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private fun calculateNextDay(currentDay: String){
        var nextDay = currentDay[0].toString()+currentDay[1]+currentDay[2]+currentDay[3]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("keek", "startted")
        if (!(PreferencesHelper.getSharedPreferenceBoolean(applicationContext, PreferencesHelper.KEY_IS_LOGINED, false))) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        lateinit var responseHomework: Response
        lateinit var responseShedule: Response

        val animations = Animations(menu, black_screen_container)

        val cont = this

        val token = PreferencesHelper.getSharedPreferenceString(applicationContext,
                PreferencesHelper.KEY_TOKEN, "error")

        val resources = resources



        //calculating 2 days range date

        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, 1)
        val d = c.time

        val dateToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val dateTomorrow = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(d)
        val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault()).format(Date())

        //


        doAsync {
            responseHomework = get("https://api.eljur.ru/api/gethomework/?" +
                    "auth_token=$token&" +
                    "devkey=8227490faaaa60bb94b7cb2f92eb08a4&" +
                    "vendor=hselyceum&" +
                    "out_format=json&" +
                    "days=$dateToday-$dateTomorrow")

            if (responseHomework.jsonObject.getJSONObject("response").getString("state") == "403"){
                uiThread {
                    startActivity(Intent().setClass(applicationContext, LoginActivity::class.java))
                }
            }
            else{
                responseShedule = get("https://api.eljur.ru/api/getschedule/?" +
                        "auth_token=$token&" +
                        "devkey=8227490faaaa60bb94b7cb2f92eb08a4&" +
                        "vendor=hselyceum&" +
                        "out_format=json&" +
                        "days=$dateToday-$dateTomorrow&" +
                        "rings=true")

                uiThread {

                    var queryDay = "1"

                    if (responseShedule.jsonObject.getJSONObject("response").getJSONObject("result")
                                    .getJSONObject("students").getJSONObject("21554")
                                    .getJSONObject("days").keys()
                                    .asSequence().contains(dateToday) &&
                            ElJurApiHelper.calculateLessonEndTime(responseShedule.jsonObject.getJSONObject("response").getJSONObject("result")
                                    .getJSONObject("students").getJSONObject("21554")
                                    .getJSONObject("days").getJSONObject(dateToday)
                                    .getJSONObject("items")) >= timeFormat){

                        queryDay = dateToday
                        header_text.text = "Домашнее задание на сегодня"
                        schedule_text.text = "Рассписание на сегодня"
                    }
                    else{
                        queryDay = dateTomorrow
                        header_text.text = "Домашнее задание на завтра"
                        schedule_text.text = "Рассписание на завтра"
                    }


                    if (responseHomework.jsonObject.getJSONObject("response").get("result") is JSONArray || !responseHomework.jsonObject.getJSONObject("response").getJSONObject("result")
                                    .getJSONObject("students").getJSONObject("21554").getJSONObject("days").has(queryDay))
                    {
                        val text = TextView(cont)
                        text.text = "Нет домашнего задания"
                        text.textSize = PixelConverter.convertDpToPixels(20f, cont).toFloat() // change
                        text.setTextColor(Color.parseColor("#000000"))
                        text.setTypeface(null, Typeface.BOLD)

                        val textParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        textParams.setMargins(0,20,0,0)

                        text.layoutParams = textParams

                        tomorrow_homework.addView(text)
                    }

                    else if (responseHomework.jsonObject.getJSONObject("response").getJSONObject("result")
                                    .getJSONObject("students").getJSONObject("21554").getJSONObject("days").has(queryDay)){
                        val homework = responseHomework.jsonObject.getJSONObject("response").getJSONObject("result")
                                .getJSONObject("students").getJSONObject("21554").getJSONObject("days").getJSONObject(queryDay).getJSONObject("items")

                        for (i in homework.keys()){
                            TextAdder.addHomework(main_block.findViewById(R.id.tomorrow_homework) ,
                                    i.toString(),
                                    homework.getJSONObject(i).getJSONObject("homework").getJSONObject("1").getString("value"),
                                    cont)
                        }
                    }

                    if (responseShedule.jsonObject.getJSONObject("response").get("result") is JSONArray || !responseHomework.jsonObject.getJSONObject("response").getJSONObject("result")
                                    .getJSONObject("students").getJSONObject("21554").getJSONObject("days").has(queryDay))
                    {
                        val text = TextView(cont)
                        text.text = "Нет уроков"
                        text.textSize = 20f
                        text.setTextColor(Color.parseColor("#000000"))
                        text.setTypeface(null, Typeface.BOLD)

                        val textParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        textParams.setMargins(0,20,0,0)

                        text.layoutParams = textParams

                        tomorrow_schedule.addView(text)
                    }

                    else{
                        val scheduleObj = responseShedule.jsonObject.getJSONObject("response").getJSONObject("result").getJSONObject("students")
                                .getJSONObject("21554").getJSONObject("days").getJSONObject(queryDay).getJSONObject("items")
                        for (i in scheduleObj.keys()){

                            TextAdder.addSchedule(main_block.findViewById(R.id.tomorrow_schedule),
                                    scheduleObj.getJSONObject(i).getString("name"), cont)
                        }
                    }


                }
            }

        }

        //setting pop up menu
        MenuPainter.paintMenu(menuOption1, menu.findViewById(R.id.main_page))

        //getting containers

        val homework_container = main_block.findViewById<LinearLayout>(R.id.tomorrow_homework)
        val schedule_containder = main_block.findViewById<LinearLayout>(R.id.tomorrow_schedule)






        showPopUpMenu.setOnClickListener {
            menu.visibility = View.VISIBLE
            menu.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.pop_up_menu_transition_up))
            animations.setAlpha(black_screen_container, 0.6f)
        }

        black_screen_container.setOnClickListener {
            if ( black_screen_container.alpha == 0.6f){
                menu.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.pop_up_menu_transition_down))
                animations.setAlpha(black_screen_container, 0f)
                menu.visibility = View.INVISIBLE
            }
        }

        main_page.setOnClickListener {
            Log.e("keek" , "lllll")
        }
        diary.setOnClickListener{
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }
        menu.setOnClickListener {
            Log.e("keek" , "lllll")
        }
        move_tool.setOnTouchListener(animations.listener)

    }



}
