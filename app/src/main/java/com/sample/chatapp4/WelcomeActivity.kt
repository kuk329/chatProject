package com.sample.chatapp4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    internal var handler = Handler()
    var firebaseUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // 삭제 코드
//        register_welcome_btn.setOnClickListener {
//            val intent = Intent(this@WelcomeActivity,RegisterActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        login_welcome_btn.setOnClickListener {
//            val intent = Intent(this@WelcomeActivity,LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

    }// end of onCreate()

    override fun onStart() {
        super.onStart()
        // 현재 로그인 상태인지 확인
        firebaseUser = FirebaseAuth.getInstance().currentUser

        handler.postDelayed({
            if(firebaseUser != null){ // already login -> go to MainActivity
                val intent = Intent(this@WelcomeActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@WelcomeActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3000)
    }
}// end of class WelcomeActivity