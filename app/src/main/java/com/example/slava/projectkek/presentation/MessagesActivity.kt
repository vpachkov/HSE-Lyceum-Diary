package com.example.slava.projectkek.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.PixelConverter
import khttp.get
import kotlinx.android.synthetic.main.activity_messages.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val context  = this
        val token = PreferencesHelper.getSharedPreferenceString(this, PreferencesHelper.KEY_TOKEN , "error")

        doAsync {
            val responseDiary = get("https://api.eljur.ru/api/getmessagereceivers/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json")
            uiThread {
                val groups = responseDiary.jsonObject.getJSONObject("response").getJSONObject("result").getJSONArray("groups")
                for ( i in 0 until groups.length()){
                    val group = LinearLayout(context)
                    group.orientation = LinearLayout.VERTICAL
                    val groupName = TextView(context)
                    groupName.text = groups.getJSONObject(i).getString("name")
                    groupName.setTextSize(TypedValue.COMPLEX_UNIT_SP , 19f)
                    groupName.setTypeface(null, Typeface.BOLD)
                    groupName.setPadding(PixelConverter.convertDpToPixels(10f, context),
                            PixelConverter.convertDpToPixels(10f, context),
                            PixelConverter.convertDpToPixels(10f, context),
                            PixelConverter.convertDpToPixels(10f, context))
                    groupName.setTextColor(Color.parseColor("#000000"))
                    group.addView(groupName)
                    groupName.setOnClickListener {
                        for (j in 0 until groups.getJSONObject(i).getJSONArray("users").length()){
                            val person = TextView(context)
                            val teacherName = groups.getJSONObject(i).getJSONArray("users").getJSONObject(j).getString("lastname") + " " +
                                    groups.getJSONObject(i).getJSONArray("users").getJSONObject(j).getString("firstname")+ " " +
                                    groups.getJSONObject(i).getJSONArray("users").getJSONObject(j).getString("middlename")
                            person.text = teacherName
                            person.setTextSize(TypedValue.COMPLEX_UNIT_SP , 16f)
                            val personParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT)
                            personParams.setMargins(0 , PixelConverter.convertDpToPixels(10f, context) , 0 , PixelConverter.convertDpToPixels(10f, context))
                            person.setBackgroundResource(R.drawable.drawable_teacher)
                            person.layoutParams = personParams
                            person.setPadding(PixelConverter.convertDpToPixels(10f, context),
                                    PixelConverter.convertDpToPixels(20f, context),
                                    PixelConverter.convertDpToPixels(10f, context),
                                    PixelConverter.convertDpToPixels(20f, context))
                            person.setTextColor(Color.parseColor("#000000"))

                            person.setOnClickListener {
                                val intent = Intent(context, ChatActivity::class.java)
                                intent.putExtra("teacherID" , groups.getJSONObject(i).getJSONArray("users").getJSONObject(j).getString("name"))
                                intent.putExtra("teacherName" , teacherName)
                                startActivity(intent)
                            }
                            group.addView(person)
                        }
                    }
                    messages_users_block.addView(group)
                }
            }

        }
    }

}