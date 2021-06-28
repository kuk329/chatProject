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

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sample.chatapp4.AdapterClasses.UserAdapter
import com.sample.chatapp4.MainActivity
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.sample.chatapp4.second.MessageChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_list.view.*
import kotlinx.android.synthetic.main.list_friend_item.view.*
import kotlinx.android.synthetic.main.list_friend_item.view.profile_image
import kotlinx.android.synthetic.main.list_friend_item.view.username


/**
 * Shows the main title screen with a button that navigates to [FriendProfileFragment].
 */
class FriendListFragment : Fragment() {

//    var refUsers : DatabaseReference? = null
//    var firebaseUser : FirebaseUser? = null
    private  var userAdapter: UserAdapter?= null
    private var mUsers: ArrayList<Users>? = null
    //private var recyclerView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)


        view.recycler_friend_list.adapter = FriendListRecyclerViewAdapter()
        view.recycler_friend_list.layoutManager= LinearLayoutManager(activity)
//        recyclerView = view.findViewById(R.id.recycler_friend_list)
//        recyclerView!!.setHasFixedSize(true)
//        recyclerView!!.layoutManager = LinearLayoutManager(context)


        return view
    }

    inner class FriendListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var users : ArrayList<Users> = arrayListOf()
        init {
            var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
            val refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users") // get all users from database
            refUsers.addValueEventListener(object: ValueEventListener
            {
                override fun onDataChange(p0: DataSnapshot)
                {
                    users.clear()
                    for(snapshot in p0.children)
                    {
                        val item = snapshot.getValue(Users::class.java)
                        if(!(item!!.uid).equals(firebaseUserID)) // except me
                        {
                            users.add(item)
                        }
                    }
                    notifyDataSetChanged()
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_friend_item,parent,false)
            return CustomViewHolder(view)

        }
        inner class CustomViewHolder(view:View): RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return users.size
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // 넘어온 데이터들 mapping
            var viewholder = (holder as CustomViewHolder).itemView
            val user = users[position]

            // UserId
           // viewholder.username.text = users[position].username
            viewholder.username.text = user.username


            // Image
            //Glide.with(holder.itemView.context).load(users[position].profile).into(viewholder.profile_image)
           // Picasso.get().load(users[position].profile).placeholder(R.drawable.profile_img).into(viewholder.profile_image)
            Picasso.get().load(user.profile).placeholder(R.drawable.profile_img).into(viewholder.profile_image)

            viewholder.setOnClickListener {
                Log.d("click","클릭함")
                val options = arrayOf<CharSequence>(
                    "메세지 보내기",
                    "프로필 보기"
                )
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("무엇을 하시겠습니까?")
                builder.setItems(options,DialogInterface.OnClickListener{ dialog, position ->

                  if(position == 0){
                      val intent = Intent(context,MessageChatActivity::class.java)
                      intent.putExtra("visit_id",user.uid) // 클릭한 친구 id값 넘김
                      val test = viewholder.username.text
                      val test2 = user.uid
                      Log.d("test"," 현재 클릭한 친구 이름 : $test")
                      Log.d("test"," 현재 클릭한 친구 아이디 : $test2")
                      context?.startActivity(intent)

                  }
                   if (position == 1){


                   }
                })
                builder.show()
            }
        }

    } // end of inner class


//
//    private fun retrieveAllUsers() {
//        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
//        val refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users") // get all users from database
//
//        refUsers.addValueEventListener(object: ValueEventListener
//        {
//            override fun onDataChange(p0: DataSnapshot)
//            {
//                (mUsers as ArrayList<Users>).clear()
//
//                    for(snapshot in p0.children)
//                    {
//                        val user: Users? = p0.getValue(Users::class.java)
//                        if(!(user!!.getUID()).equals(firebaseUserID)) // except me
//                        {
//                            (mUsers as ArrayList<Users>).add(user)
//                        }
//                    }
//                    userAdapter?.notifyDataSetChanged()
//            }
//            override fun onCancelled(p0: DatabaseError) {
//            }
//        })
//        userAdapter = UserAdapter(requireContext(), (mUsers as ArrayList<Users>)!!,false)
//        recyclerView!!.adapter = userAdapter
//        Log.d("테스트","2")
//
//    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { // 메뉴 생성
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.search_menu,menu) // 메뉴 연결
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 메뉴가 선택됬을때 NavigationUI를 이용해 id가 동일한 navigation 메뉴로 이동
//        return NavigationUI.onNavDestinationSelected(item!!,
//            requireView().findNavController())||super.onOptionsItemSelected(item)
//    }
}




