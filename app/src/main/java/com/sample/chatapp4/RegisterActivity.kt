package com.sample.chatapp4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers : DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        mAuth = FirebaseAuth.getInstance()

        // 회원가입 버튼 클릭시
        register_button.setOnClickListener {
            registerUser() // 사용자 정의 method

        }

        login_text.setOnClickListener {
            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        val username : String = username_register.text.toString()
        val email : String = email_register.text.toString()
        val password : String = password_register.text.toString()

        if(username == "") // 예외처리
        {
            Toast.makeText(this@RegisterActivity,"이름을 입력하세요.",Toast.LENGTH_SHORT).show()

        }else if(email == "")
        {
            Toast.makeText(this@RegisterActivity,"이메일을 입력하세요.",Toast.LENGTH_SHORT).show()
        }else if(password == "")
        {
            Toast.makeText(this@RegisterActivity,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()

        }else  // save in database
        {
            mAuth.createUserWithEmailAndPassword(email,password) // 입력한 값으로 회원정보 db 생성
                .addOnCompleteListener{task ->
                    if(task.isSuccessful)
                    {
                        Log.d("tag","db 생성 성공")
                        firebaseUserID = mAuth.currentUser!!.uid
                        Log.d("tag",firebaseUserID)
                        refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").getReference().child("Users").child(firebaseUserID)
                        Log.d("tag","db에서 해당 유저의 정보 가져옴")
                        val userHashMap = HashMap<String,Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/messengerapp-45874.appspot.com/o/profile_img.png?alt=media&token=362a4bb1-6ee6-45f2-85ab-ed544521c63b"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/messengerapp-45874.appspot.com/o/cover.jpg?alt=media&token=01bc16c9-3c45-435d-bfcd-b7705ba28a3d"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://www.google.com"

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful)
                                {
                                    val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                                    // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity,"회원가입 실패 : "+task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }


        }

    }// end of fun registerUser()

}// end of class RegisterActivity