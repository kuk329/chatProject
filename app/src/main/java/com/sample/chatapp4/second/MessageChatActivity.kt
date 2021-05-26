package com.sample.chatapp4.second

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity:AppCompatActivity(){

    var userIdVisit : String = ""
    var firebaseUser : FirebaseUser?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id")!!
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val reference = FirebaseDatabase.getInstance().reference
            .child("Users").child(userIdVisit)
        reference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
               val user: Users? = p0.getValue(Users::class.java)

                username_mchat.text = user!!.getUserName()
                Picasso.get().load(user.getProfile()).into(profile_image_mchat)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        send_message_btn.setOnClickListener{
            val message = text_message.text.toString() // 입력한 메세지 받아와서 message 변수에 저장
            if(message==""){ // 메세지 칸이 비었을때
                Toast.makeText(this@MessageChatActivity,"메세지를 입력해 주세요 ",Toast.LENGTH_LONG).show()
            }else{
                sendMessageToUser(firebaseUser!!.uid,userIdVisit,message)
            }
        }
        attach_image_file_btn.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"이미지를 골라주세요"),438)
        }
    }

    private fun sendMessageToUser(senderId: String, receiverId: String, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String,Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener{task ->
                if(task.isSuccessful)
                {
                    val chatsListReference = FirebaseDatabase.getInstance()
                        .reference
                        .child("ChatLists")

                    // implement the push notifications using fcm
                    chatsListReference.child("id").setValue(firebaseUser!!.uid)
                    val reference = FirebaseDatabase.getInstance().reference
                        .child("Users").child(firebaseUser!!.uid)
                }

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==438&& resultCode == Activity.RESULT_OK && data!!.data!= null){
            val loadingBar = ProgressDialog(application)
            loadingBar.setMessage("Please wait, image is sending...")
            loadingBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

        }

    }


}