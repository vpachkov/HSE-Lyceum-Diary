package com.example.slava.projectkek.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
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
                    group.addView(groupName)
                    groupName.setOnClickListener {
                        for (j in 0 until groups.getJSONObject(i).getJSONArray("users").length()){
                            val person = TextView(context)
                            person.text = groups.getJSONObject(i).getJSONArray("users").getJSONObject(j).getString("lastname")
                            group.addView(person)
                        }
                    }
                    messages_users_block.addView(group)
                }
            }

        }
    }

}