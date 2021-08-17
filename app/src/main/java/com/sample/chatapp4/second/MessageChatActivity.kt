package com.sample.chatapp4.second

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.renderscript.Sampler
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.sample.chatapp4.AdapterClasses.ChatsAdapter
import com.sample.chatapp4.MainActivity
import com.sample.chatapp4.ModelClasses.Chat
import com.sample.chatapp4.ModelClasses.Users

import com.sample.chatapp4.R
import com.sample.chatapp4.WelcomeActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity:AppCompatActivity(){


    var userIdVisit : String = ""  // 상대방(프로필 클릭시)
    var firebaseUser : FirebaseUser?= null
    var chatsAdapter: ChatsAdapter? = null
    var mChatList: List<Chat>? = null
    lateinit var  recycler_view_chats : RecyclerView

    var reference : DatabaseReference? = null

    var notify = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar_message_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@MessageChatActivity, WelcomeActivity::class.java)
            // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id")!! // 상대방 id
        firebaseUser = FirebaseAuth.getInstance().currentUser // 내 정보 가져옴(로그인한 사용자)

        recycler_view_chats = findViewById(R.id.recycler_view_chats)
        recycler_view_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycler_view_chats.layoutManager = linearLayoutManager


        // 상단 사용자 표시하는 코드
         reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
            .child("Users").child(userIdVisit)
        reference!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
               val user: Users? = p0.getValue(Users::class.java)
                username_mchat.text = user?.username
                Picasso.get().load(user?.profile).into(profile_image_mchat)

                retrieveMessages(firebaseUser!!.uid,userIdVisit,user?.profile)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // send message button click
        send_message_btn.setOnClickListener{
            notify=true // 메세지 보낼때
            val message = text_message.text.toString() // 입력한 메세지 받아와서 message 변수에 저장

            if(message==""){ // 메세지 칸이 비었을때
                Toast.makeText(this@MessageChatActivity,"메세지를 입력해 주세요 ",Toast.LENGTH_LONG).show()
            }else{
                // sendMessageToUser  함수실행 -> 내 아이디, 상대방 아이디, 메세지 내용 넘김
                sendMessageToUser(firebaseUser!!.uid,userIdVisit,message)
            }
            text_message.setText("")
        }
        // 사진 첨부 button click
        attach_image_file_btn.setOnClickListener{
            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"이미지를 골라주세요"),438)
        }

        seenMessage(userIdVisit)
    } // end of onCreate

    private fun sendMessageToUser(senderId: String, receiverId: String, message: String) {

        val reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String,Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false  // 메시지를 읽었는지 확인
        messageHashMap["url"] = ""  // 이미지
        messageHashMap["messageId"] = messageKey // 메세지들 구별 id
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener{task ->
                if(task.isSuccessful)
                {
                    val chatsListReference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/")
                        .reference
                        .child("ChatList")
                        .child(firebaseUser!!.uid)
                        .child(userIdVisit)

                    chatsListReference.addListenerForSingleValueEvent(object:ValueEventListener{

                        override fun onDataChange(p0: DataSnapshot) {
                            if(!p0.exists()){

                                chatsListReference.child("id").setValue(userIdVisit)
                            }

                            val chatsListReceiverRef = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/")
                                .reference
                                .child("ChatList")
                                .child(userIdVisit)
                                .child(firebaseUser!!.uid)
                            chatsListReceiverRef.child("id").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })

                    // push notifications using fcm
//                    val reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
//                        .child("Users").child(firebaseUser!!.uid)
//                    reference.addValueEventListener(object: ValueEventListener{
//                        override fun onDataChange(p0: DataSnapshot) {
//
//                            var user = p0.getValue(Users::class.java)
//                            if(notify){
//                                sendNotification(receiverId,user!!.username,message)
//                            }
//                            notify=false
//                        }
//
//                        override fun onCancelled(p0: DatabaseError) {
//
//                        }
//                    })
                }
            }
    }

//    private fun sendNotification(receiverId: String, username: String?, message: String) {
//
//        val ref = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
//            .child("Tokens")
//
//        val query = ref.orderByKey().equalTo(receiverId)
//
//        query.addValueEventListener(object:ValueEventListener{
//            override fun onDataChange(p0: DataSnapshot) {
//
//                for(dataSnapshot in p0.children){
//                    val token: Token? = dataSnapshot.getValue(Token::class.java)
//
//                    val data = Data(firebaseUser!!.uid,R.mipmap.ic_launcher,"")
//                }
//
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==438&& resultCode ==RESULT_OK && data!=null && data!!.data!= null)
        {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("이미지 전송중입니다... ")
            progressBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

            var uploadTask : StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task->
                if(task.isSuccessful)
                {
                    task.exception?.let{
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener{task ->
                if(task.isSuccessful)
                {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String,Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "sent you an image"
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["isseen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                progressBar.dismiss()
                                // push notifications using fcm
//                                val reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference
//                                    .child("Users").child(firebaseUser!!.uid)
//                                reference.addValueEventListener(object: ValueEventListener{
//                                    override fun onDataChange(p0: DataSnapshot) {
//
//                                        var user = p0.getValue(Users::class.java)
//                                        if(notify){
//                                            sendNotification(userIdVisit,user!!.username,"사진을 보냈습니다.")
//                                        }
//                                        notify=false
//                                    }
//
//                                    override fun onCancelled(p0: DatabaseError) {
//
//                                    }
//                                })
                            }
                        }

                    progressBar.dismiss()

                }
            }


        }

    }
    private fun retrieveMessages(senderId: String, receiverId: String, receiverImageUrl: String?)
    {
        mChatList = ArrayList()
        val reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/")
            .reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for(snapshot in p0.children){
                    val chat = snapshot.getValue(Chat::class.java)

                    if(chat!!.receiver.equals(senderId) && chat.sender.equals(receiverId)
                        || chat.receiver.equals(receiverId)&& chat.sender.equals(senderId)){
                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    chatsAdapter = ChatsAdapter(this@MessageChatActivity,(mChatList as ArrayList<Chat>),
                    receiverImageUrl!!)

                    recycler_view_chats.adapter = chatsAdapter


                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    var seenListener:ValueEventListener? = null
    private fun seenMessage(userId:String){
        val reference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/")
            .reference.child("Chats")

        seenListener = reference!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                for(dataSnapshot in p0.children){
                    val chat = dataSnapshot.getValue(Chat::class.java)

                    if(chat!!.receiver.equals(firebaseUser!!.uid)&& chat!!.sender.equals(userId)){
                        val hashMap = HashMap<String,Any>()
                        hashMap["isseen"]=true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }// end of seenMessage()

    override fun onPause() {
        super.onPause()

        reference!!.removeEventListener(seenListener!!)
    }


}// end of class