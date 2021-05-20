package com.sample.chatapp4.second

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.chatapp4.R
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        send_message_btn.setOnClickListener{
            val message = text_message.text.toString() // 입력한 메세지 받아와서 message 변수에 저장
            if(message==""){ // 메세지 칸이 비었을때
                Toast.makeText(this@MessageChatActivity,"메세지를 입력해 주세요 ",Toast.LENGTH_LONG).show()
            }else{
         //       sendMessageToUser() // 사용자 정의 함수
            }

        }
    }
}