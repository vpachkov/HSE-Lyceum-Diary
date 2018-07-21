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
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import khttp.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.black_screen.*
import org.jetbrains.anko.doAsync
import kotlinx.android.synthetic.main.pop_up_menu.*
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

        doAsync {
            val lol = get("https://api.eljur.ru/api/getschedule", data = params)
            Log.e("keek" , lol.text)
        }
        //setting pop up menu
        val hide_menu = menu.findViewById<TextView>(R.id.main_page)
        hide_menu.setTypeface(null , Typeface.BOLD)
        hide_menu.setBackgroundResource(R.drawable.gradient_menu_item_rounded)
        hide_menu.setTextColor(Color.parseColor("#ffffff"))



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
            val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.pop_up_menu_transition_up)
            //val animator = menu.animate()
            menu.visibility = View.VISIBLE
            black_screen_container.elevation = 1f
            //showFromBottom(menu)
            menu.startAnimation(anim)
            setAlpha(black_screen_container, 0.6f)
        }

        black_screen_container.setOnClickListener {
            if ( black_screen_container.alpha == 0.6f){
                menu.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.pop_up_menu_transition_down))
                setAlpha(black_screen_container, 0f)
                menu.visibility = View.INVISIBLE
            }
        }

        main_page.setOnClickListener {
            Log.e("keek" , "lllll")
        }
        menu.setOnClickListener {
            Log.e("keek" , "lllll")
        }
        move_tool.setOnTouchListener(listener)

    }

    var toTop = 0F
    var ignoreTouch = false
    var wereGesture = false
    var firstTouch = 0F
    var positionMenu = 0F

    var listener = View.OnTouchListener(function = {_, motionEvent ->

        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            toTop = motionEvent.rawY - menu.y
            ignoreTouch = false
            wereGesture = false
            firstTouch = motionEvent.rawY
            positionMenu = menu.y
        }

        if (motionEvent.action == MotionEvent.ACTION_MOVE) {
            if (ignoreTouch == false) {
                wereGesture = true
                if (motionEvent.rawY - toTop >= black_screen_container.height - menu.height) {
                    menu.y = motionEvent.rawY - toTop
                } else {
                    menu.y = (black_screen_container.height - menu.height).toFloat()
                }
            }
        }

        if (motionEvent.action == MotionEvent.ACTION_UP){
            if (motionEvent.rawY - firstTouch > 50) {
                hideFromBottom(menu, positionMenu)
                wereGesture = false
            } else {
                showFromBottom(menu)
            }
        }

        true

    })

    fun setAlpha(v: View, alpha: Float){
        val animator = v.animate()
        animator.alpha(alpha).duration = 200
        if (alpha == 0f)
            animator.withEndAction {
                v.elevation = -1f
            }
        else
            animator.withEndAction {
                v.elevation = 1f
            }
    }

    fun showFromBottom(v: View) {
        v.visibility = View.VISIBLE
        val animator = v.animate()
        animator.translationY(0F).duration = 200

        setAlpha(black_screen_container, 0.6f)

    }

    fun hideFromBottom(v: View, pos: Float) {
        val animator = v.animate()
        animator.translationY(v.y)
                .setDuration(200)
                .withEndAction {
                    v.y = pos
                    v.visibility = View.INVISIBLE
                }

        setAlpha(black_screen_container, 0f)
    }



}