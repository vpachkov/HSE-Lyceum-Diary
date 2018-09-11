package com.example.slava.projectkek.presentation

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import com.eclipsesource.json.JsonObject
import com.example.slava.projectkek.R
import com.example.slava.projectkek.data.PreferencesHelper
import com.example.slava.projectkek.domain.utils.TextAdder
import khttp.get
import khttp.post
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.zip.Inflater

class ChatActivity : AppCompatActivity() {

    private var messagesList: MutableList<Pair<Pair<String , String> , Boolean>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val context  = this
        val token = PreferencesHelper.getSharedPreferenceString(this, PreferencesHelper.KEY_TOKEN , "error")

        var message = " "
        var subject = " "

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
                        messagesList.add(Pair(Pair(inboxMessages.optJSONObject(i).getString("id") ,inboxMessages.optJSONObject(i).getString("date")), true))

                for (i in 0 until sentMessages.length())
                    for (j in 0 until sentMessages.getJSONObject(i).getJSONArray("users_to").length())
                        if (sentMessages.getJSONObject(i).getJSONArray("users_to").getJSONObject(j).getString("name") == teacherId && !sentMessages.isNull(i)) {
                            messagesList.add(Pair(Pair(sentMessages.optJSONObject(i).getString("id") ,sentMessages.optJSONObject(i).getString("date")), false))
                            break
                        }

                sortMessages(0 , messagesList.size - 1)

                parseKek(0 , context, token)


            }

        }

        send_message.setOnClickListener {
            message = print_line.text.toString()
            if (message.isEmpty())
                message = print_line.hint.toString()

            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val bg = layoutInflater.inflate(R.layout.dialog_subject , null)
            dialogBuilder.setView(bg)
            val dialog = dialogBuilder.create()

            dialog.window.setBackgroundDrawable(getResources().getDrawable(R.drawable.alert_dialog_gradient))
            dialog.show()
            bg.findViewById<Button>(R.id.dialog_ok).setOnClickListener {
                subject = bg.findViewById<EditText>(R.id.subject).text.toString()

                if (subject.isEmpty())
                    subject = bg.findViewById<EditText>(R.id.subject).hint.toString()

                dialog.cancel()
                sendMessage(token, message , subject , teacherId ,context)
                print_line.text.clear()
            }



        }
    }

    private fun sendMessage(token: String , message: String , subjectName:String , teacherId: String , context: Context){
        doAsync {
            val responseInboxMessages = post("https://api.eljur.ru/api/sendmessage/?auth_token=$token&devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json&" +
                    "subject=$subjectName&text=$message&users_to=$teacherId")
            onComplete {
                val messageBlock = TextAdder.makeChatMessage(message , context , false)
                val cont = RelativeLayout(context)
                cont.gravity = Gravity.END
                cont.addView(messageBlock)
                chat_block.addView(cont)
            }
        }
    }

    private fun parseKek(number: Int , context: Context , token: String){
        if (number >= messagesList.size) return
        val message = messagesList[number]
        if (message.second){
            doAsync {
                val messageResponse = get("https://api.eljur.ru/api/getmessageinfo/?auth_token=$token&" +
                        "devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json&id=${message.first.first}")


                uiThread {
                    chat_block.addView(
                            TextAdder.makeChatMessage(
                                    messageResponse.jsonObject.getJSONObject("response").getJSONObject("result").getJSONObject("message").getString("text"),
                                    context,
                                    true
                            )
                    )

                    onComplete {
                        parseKek(number+1 , context , token)
                    }
                }
            }
        }
        else {
            doAsync {

                val messageResponse = get("https://api.eljur.ru/api/getmessageinfo/?auth_token=$token&" +
                        "devkey=8227490faaaa60bb94b7cb2f92eb08a4&vendor=hselyceum&out_format=json&id=${message.first.first}")

                uiThread {
                    val cont = RelativeLayout(context)
                    cont.gravity = Gravity.END
                    cont.addView(
                            TextAdder.makeChatMessage(
                                    messageResponse.jsonObject.getJSONObject("response").getJSONObject("result").getJSONObject("message").getString("text"),
                                    context,
                                    false
                            )
                    )
                    chat_block.addView(cont)

                    onComplete {
                        parseKek(number+1 , context , token)
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
            while (messagesList[l].first.second < mid.first.second)
                l++
            while (messagesList[r].first.second > mid.first.second)
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
