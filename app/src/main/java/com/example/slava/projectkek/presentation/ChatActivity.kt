package com.example.slava.projectkek.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.RelativeLayout
import com.eclipsesource.json.JsonObject
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.TextAdder
import khttp.get
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    private var messagesList: MutableList<JSONObject> = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val context  = this
        val token = PreferencesHelper.getSharedPreferenceString(this, PreferencesHelper.KEY_TOKEN , "error")

        val teacherId = getIntent().getExtras().getString("teacherID")
        val teacherName = getIntent().getExtras().getString("teacherName")

        teacher_name.text = teacherName

        doAsync {
            val responseInboxMessages = get("https://api.eljur.ru/api/getmessages/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json&folder=inbox")
            val responseSentMessages = get("https://api.eljur.ru/api/getmessages/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json&folder=sent")
            uiThread {
                val inboxMessages = responseInboxMessages.jsonObject.getJSONObject("response")
                        .getJSONObject("result").getJSONArray("messages")
                val sentMessages = responseSentMessages.jsonObject.getJSONObject("response")
                        .getJSONObject("result").getJSONArray("messages")
                for (i in 0 until inboxMessages.length())
                    if (inboxMessages.getJSONObject(i).getJSONObject("user_from").getString("name") == teacherId && !inboxMessages.isNull(i))
                        messagesList.add(inboxMessages.optJSONObject(i))
                for (i in 0 until sentMessages.length())
                    for (j in 0 until sentMessages.getJSONObject(i).getJSONArray("users_to").length())
                    if (sentMessages.getJSONObject(i).getJSONArray("users_to").getJSONObject(j).getString("name") == teacherId && !sentMessages.isNull(i)) {
                        messagesList.add(sentMessages.optJSONObject(i))
                        break
                    }

                sortMessages(0 , messagesList.size - 1)

                for (message in messagesList){
                    if (message.has("user_from"))
                        chat_block.addView(TextAdder.makeChatMessage(
                                message.getString("short_text") ,
                                context,
                                true
                        ))
                    else if (message.has("users_to")) {
                        val cont = RelativeLayout(context)
                        cont.gravity = Gravity.END

                        cont.addView(TextAdder.makeChatMessage(
                                message.getString("short_text"),
                                context,
                                false
                        ))

                        chat_block.addView(cont)
                    }

                }


            }

        }
    }

    private fun sortMessages(b: Int, e: Int){
        if (messagesList.size == 0) return
        var l = b
        var r = e
        val mid = messagesList[(l+r)/2]
        while (l <= r){
            while (messagesList[l] < mid)
                l++
            while (messagesList[r] > mid)
                r--
            if (l <= r)
                swapMessages(l++ , r--)

        }
        if (b < r)
            sortMessages(b ,r)
        if (e > l)
            sortMessages(l ,e)
    }

    private fun swapMessages(l: Int , r: Int){
        val copy_l = messagesList[l]
        messagesList[l] = messagesList[r]
        messagesList[r] = copy_l
    }

}

private operator fun JSONObject.compareTo(mid: JSONObject): Int {
    val thisDate = this.getString("date")
    val midDate = mid.getString("date")

    return when {
        thisDate > midDate -> 1
        thisDate == midDate -> 0
        else -> -1
    }
}

