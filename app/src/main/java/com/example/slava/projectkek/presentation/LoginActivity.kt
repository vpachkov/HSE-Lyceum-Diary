package com.example.slava.projectkek.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.RelativeLayout
import com.eclipsesource.json.Json
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.data.model.token.Token
import com.google.gson.Gson
import khttp.get
import khttp.post
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.input_line.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        main_window_login.findViewById<RelativeLayout>(R.id.login_button).setOnClickListener {
            val input_login = main_window_login.findViewById<EditText>(R.id.login_field).text.toString()
            val input_password = main_window_login.findViewById<EditText>(R.id.password_field).text.toString()
            val mainUrl = "https://api.eljur.ru/api/auth/"
            var params = mapOf(
                    "vendor" to "hselyceum",
                    "devkey" to "8227490faaaa60bb94b7cb2f92eb08a4",
                    "out_format" to "json",
                    "login" to input_login,
                    "password" to input_password
            )
            doAsync {
                val lol = post(mainUrl, data = params)
                Log.e("keek", lol.text)
                val kek = Json.parse(lol.text).asObject().get("response").asObject().get("result").asObject().get("token").asString()
                Log.e("keek", kek)
                PreferencesHelper.setSharedPreferenceString(applicationContext, PreferencesHelper.KEY_TOKEN, kek)
                PreferencesHelper.setSharedPreferenceBoolean(applicationContext, PreferencesHelper.KEY_IS_LOGINED, true)
                uiThread {
                    val intent_name = Intent()
                    intent_name.setClass(applicationContext, MainActivity::class.java!!)
                    startActivity(intent_name)
                }
            }
        }
        /*
        signUp.setOnClickListener {
            val Login = login.text.toString()
            val Password = password.text.toString()
            val mainUrl = "https://api.eljur.ru/api/auth/"
            var params = mapOf(
                    "vendor" to "hselyceum",
                    "devkey" to "8227490faaaa60bb94b7cb2f92eb08a4",
                    "out_format" to "json",
                    "login" to Login,
                    "password" to Password
            )
            doAsync {
                val lol = get(mainUrl, data = params)
                val obj = Gson().fromJson(lol.text, Token::class.java)
                PreferencesHelper.setSharedPreferenceString(applicationContext, PreferencesHelper.KEY_TOKEN, obj!!.response!!.result!!.token.toString())
                PreferencesHelper.setSharedPreferenceBoolean(applicationContext, PreferencesHelper.KEY_IS_LOGINED, true)
                uiThread {
                    val intent_name = Intent()
                    intent_name.setClass(applicationContext, MainActivity::class.java!!)
                    startActivity(intent_name)
                }
            }
        }

        */
    }
}
