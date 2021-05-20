package com.sample.chatapp4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        mAuth = FirebaseAuth.getInstance()
        login_button.setOnClickListener {
            loginUser()
        }

        sigin_up_text.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }// end of onCreate()

    private fun loginUser() {
        // 입력한 이메일, 비밀번호 값 저장
        val email :String = email_login.text.toString()
        val password : String = password_login.text.toString()

        if(email == "") // 예외처리
        {
            Toast.makeText(this@LoginActivity,"이메일을 입력하세요.", Toast.LENGTH_SHORT).show()

        }else if(password == "")
        {
            Toast.makeText(this@LoginActivity,"비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
        else
        {
            // 입력한 값으로 db 로그인
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else
                {
                    Toast.makeText(this@LoginActivity,"로그인 실패 : "+task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                }
            }

        }

    }

}// end of class LoginActivity