/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.chatapp4.firstscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sample.chatapp4.AdapterClasses.UserAdapter
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_list.view.*


/**
 * Shows the main title screen with a button that navigates to [FriendProfileFragment].
 */
class FriendListFragment : Fragment() {

//    var refUsers : DatabaseReference? = null
//    var firebaseUser : FirebaseUser? = null
    private  var userAdapter: UserAdapter?= null
    private var mUsers: List<Users>? = null
    private var recyclerView : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {// 프래그먼트를 생성하면서 초기화 해야하는 리소스 값이나 넘겨준 값들
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)


        recyclerView = view.findViewById(R.id.recycler_friend_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)


        mUsers= ArrayList()
        retrieveAllUsers()

      //   setHasOptionsMenu(true)  // 액티비티보다 프래그먼트 메뉴가 우선



        return view

    }
    private fun retrieveAllUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users") // get all users from database
  //      val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList<Users>).clear()

                    for(snapshot in p0.children)
                    {
                        val user: Users? = p0.getValue(Users::class.java)
                        if(!(user!!.getUID()).equals(firebaseUserID)) // except me
                        {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }

                    Log.d("error", "mUsers"+mUsers.toString())

                    userAdapter = UserAdapter(context!!, (mUsers as ArrayList<Users>)!!,false)

                    recyclerView!!.adapter = userAdapter



            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { // 메뉴 생성
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_menu,menu) // 메뉴 연결
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 메뉴가 선택됬을때 NavigationUI를 이용해 id가 동일한 navigation 메뉴로 이동
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())||super.onOptionsItemSelected(item)
    }
}




