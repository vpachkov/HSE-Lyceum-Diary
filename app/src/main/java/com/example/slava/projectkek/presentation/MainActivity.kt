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
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonObject
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.Animations
import com.example.slava.projectkek.domain.utils.MenuPainter
import com.example.slava.projectkek.domain.utils.TextAdder
import khttp.get
import khttp.responses.Response
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.black_screen.*
import org.jetbrains.anko.doAsync
import kotlinx.android.synthetic.main.pop_up_menu.*
import org.jetbrains.anko.uiThread
import java.text.ParsePosition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("keek", "startted")
        if (!(PreferencesHelper.getSharedPreferenceBoolean(applicationContext, PreferencesHelper.KEY_IS_LOGINED, false))) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        var paramss = mapOf(
                "vendor" to "hselyceum",
                "devkey" to "8227490faaaa60bb94b7cb2f92eb08a4",
                "out_format" to "json",
                "auth_token" to PreferencesHelper.getSharedPreferenceString(applicationContext,
                                                                            PreferencesHelper.KEY_TOKEN, "error")

        )



        lateinit var responseHomework: Response
        lateinit var responseShedule: Response

        val animations = Animations(menu, black_screen_container)

        val cont = this

        val token = PreferencesHelper.getSharedPreferenceString(applicationContext,
                PreferencesHelper.KEY_TOKEN, "error")


        doAsync {
            Log.e("keek" , "kekkekekekkse")
            responseHomework = get("https://api.eljur.ru/api/gethomework/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json")
            responseShedule = get("https://api.eljur.ru/api/getschedule/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json")
            Log.e("keek" , responseHomework.jsonObject.getJSONObject("response").getString("state"))
            uiThread {
                //val homework = responseHomework.jsonObject.getJSONObject("response").getJSONObject("result").
                  //      getJSONObject("students").getJSONObject("386").getJSONObject("days").
                    //    getJSONObject("20160118").getJSONObject("items")

                //val shedule = responseShedule.jsonObject.getJSONObject("response").getJSONObject("result").
                   //     getJSONObject("students").getJSONObject("21554").getJSONObject("days").
                     //   getJSONObject("20180901").getJSONObject("items")

                //for (i in homework.keys()){
                  //  TextAdder.addHomework(main_block.findViewById(R.id.tomorrow_homework) ,
                    //        i.toString(),
                      //      homework.getJSONObject(i).getJSONObject("homework").getJSONObject("1").getString("value"),
                        //    cont)
                //}

               // for (i in shedule.keys()){

                  //  TextAdder.addSchedule(main_block.findViewById(R.id.tomorrow_schedule),
                         //   shedule.getJSONObject(i).getString("name"), cont)
               // }



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
