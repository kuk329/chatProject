package com.sample.chatapp4

import android.os.Bundle
import android.util.Log
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
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    var refUsers : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("tag","MainActivity 시작")
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
            Log.d("tag","bottomNav 세팅")
        } // Else, need to wait for onRestoreInstanceState

//        firebaseUser = FirebaseAuth.getInstance().currentUser
////        refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users").child(firebaseUser!!.uid)
////
////        // display username and profile picture
////        refUsers!!.addValueEventListener(object : ValueEventListener{
////
////            override fun onDataChange(p0: DataSnapshot) {
////                if(p0.exists())
////                {
////                    val user: Users? = p0.getValue(Users::class.java)
////                    사용자 이름아이디.text = user!!.getUserName()
////                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_img).into(image아이디)
////
////
////                }
////
////            }
////            override fun onCancelled(p0: DatabaseError) {
////
////            }
////        })

    }// end of onCreate()

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.first, R.navigation.list, R.navigation.third)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
//
//    internal class ViewPagerAdapter(fragmentManager:FragmentManager):
//        FragmentPagerAdapter(fragmentManager){
//        private val fragments:ArrayList<Fragment>
//        private val titles:ArrayList<String>
//        init{
//            fragments = ArrayList<Fragment>()
//            titles = ArrayList<String>()
//        }
//        override fun getCount(): Int {
//            return fragments.size
//        }
//
//        override fun getItem(position: Int): Fragment {
//           return fragments[position]
//        }
//        fun addFragment(fragment:Fragment,title:String){
//            fragments.add(fragment)
//            titles.add(title)
//        }
//
//        override fun getPageTitle(i: Int): CharSequence? {
//            return titles[i]
//        }
//
//    }
}