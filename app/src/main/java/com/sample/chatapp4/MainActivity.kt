package com.sample.chatapp4

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.firstscreen.FriendListFragment
import com.sample.chatapp4.second.ChatListFragment
import com.sample.chatapp4.thirdscreen.SettingProfileFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

        val friendListFragment = FriendListFragment()
        val chatListFragment = ChatListFragment()
        val settingProfileFragment = SettingProfileFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(friendListFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.friendList-> replaceFragment(friendListFragment)
                R.id.chatList-> replaceFragment(chatListFragment)
                R.id.myPage-> replaceFragment(settingProfileFragment)

            }
            true
        }
        } // end of onCreate


    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer,fragment)
                commit()
            }
    }
}// end of Class