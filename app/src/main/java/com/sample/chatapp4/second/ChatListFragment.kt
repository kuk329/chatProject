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

package com.sample.chatapp4.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultRegistry
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.chatapp4.AdapterClasses.UserAdapter
import com.sample.chatapp4.ModelClasses.Chatlist
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R


// 메세지 목록
class ChatListFragment : Fragment() {

    private var userAdapter : UserAdapter? = null
    private var mUsers : List<Users>? = null
    private var usersChatList : List<Chatlist>? = null
    lateinit var recycler_view_chatlist : RecyclerView
    private var firebaseUser : FirebaseUser? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_chat_list, container, false)

        recycler_view_chatlist = view.findViewById(R.id.recycler_view_chatlist)
        recycler_view_chatlist.setHasFixedSize(true)
        recycler_view_chatlist.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        usersChatList = ArrayList()

        val ref = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("ChatList")
            .child(firebaseUser!!.uid)

        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (usersChatList as ArrayList).clear()

                for(dataSnapshot in p0.children)
                {
                    val chatlist = dataSnapshot.getValue(Chatlist::class.java)

                    (usersChatList as ArrayList).add(chatlist!!)
                }
                retrieveChatLists()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        return view

    }
    private fun retrieveChatLists(){
        mUsers = ArrayList()

        val ref = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users")
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList).clear()

                for(dataSnapshot in p0.children){
                    val user = dataSnapshot.getValue(Users::class.java)

                    for(eachChatList in usersChatList!!)
                    {
                         if(user!!.uid.equals(eachChatList.getId()))
                         {
                             (mUsers as ArrayList).add(user!!)
                         }
                    }
                }

                userAdapter = UserAdapter(context!!,(mUsers as ArrayList<Users>),true)
                recycler_view_chatlist.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


}


