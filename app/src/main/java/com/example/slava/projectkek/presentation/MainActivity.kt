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
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.Animations
import com.example.slava.projectkek.domain.utils.MenuPainter
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

        var params = mapOf(
                "devkey" to "8227490faaaa60bb94b7cb2f92eb08a4",
                "vendor" to "hselyceum",
                "out_format" to "json",
                "days" to "20180201-20180207",
                "auth_token" to PreferencesHelper.getSharedPreferenceString(applicationContext,
                                                                            PreferencesHelper.KEY_TOKEN, "error")

        )

        lateinit var responseHomework: Response
        val animations = Animations(menu, black_screen_container)

        doAsync {
            Log.e("keek" , "kekkekekekkse")
            responseHomework = get("http://10.0.2.2:8000/api/gethomework/")
            //Log.e("keek" , Json.parse(lol.text).asObject().get("response").asObject().get("state").asInt().toString())
            uiThread {
               //Json.parse(responseHomework.text).asObject().get("response").asObject().get
            }
        }

        //setting pop up menu
        MenuPainter.paintMenu(menuOption1, menu.findViewById(R.id.main_page))

        //getting containers

        var homework_container = main_block.findViewById<LinearLayout>(R.id.tomorrow_homework)
        var schedule_containder = main_block.findViewById<LinearLayout>(R.id.tomorrow_schedule)

        val kek = LinearLayout(this)

        val sub_view_subject = TextView(this)


        sub_view_subject.text = "Русский язык: "
        sub_view_subject.textSize = 20f
        sub_view_subject.setTypeface(null, Typeface.BOLD)
        sub_view_subject.setTextColor(Color.parseColor("#000000"))

        val sub_view_excercise = TextView(this)

        sub_view_excercise.text = "задание в тетради ахахахахаххахахахахах ахвхавых оллол дл кек "
        sub_view_excercise.textSize = 20f
        kek.orientation = LinearLayout.VERTICAL
        kek.addView(sub_view_subject)
        kek.addView(sub_view_excercise)

        kek.setPadding(0 , 20 , 0 , 0)

        val subject = TextView(this)

        subject.text = "Русский язык"
        subject.textSize = 20f
        subject.setTypeface(null, Typeface.BOLD)
        subject.setTextColor(Color.parseColor("#000000"))
        subject.setPadding(0 , 20 , 0 , 0)

        homework_container.addView(kek)
        schedule_containder.addView(subject)


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
        menu.setOnClickListener {
            Log.e("keek" , "lllll")
        }
        move_tool.setOnTouchListener(animations.listener)

    }



}
