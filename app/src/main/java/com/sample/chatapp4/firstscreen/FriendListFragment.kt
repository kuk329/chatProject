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
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_list.view.*


/**
 * Shows the main title screen with a button that navigates to [FriendProfileFragment].
 */
class FriendListFragment : Fragment() {

    var refUsers : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {// 프래그먼트를 생성하면서 초기화 해야하는 리소스 값이나 넘겨준 값들
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)
        Log.d("tag","Title 화면 올라감")

        setHasOptionsMenu(true)  // 액티비티보다 프래그먼트 메뉴가 우선

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users").child(firebaseUser!!.uid)

        // display username and profile picture
        refUsers!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val user: Users? = p0.getValue(Users::class.java)
                    Log.d("test", user.toString())
                    view.my_name.text = user!!.getUserName()
                    Log.d("test11","이름:"+user.getUserName())
                    val text=Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_img).into(view.my_image)
                    Log.d("test11","사진 url:"+user.getProfile())
                }

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

        val viewAdapter = MyAdapter2(Array(10) { "친구 ${it + 1}" })

        view.findViewById<RecyclerView>(R.id.recycler_friend_list).run {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
        return view
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

class MyAdapter2(private val myDataset: Array<String>) :
    RecyclerView.Adapter<MyAdapter2.ViewHolder2>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder2(val item: View) : RecyclerView.ViewHolder(item)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder2 {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_friend_item, parent, false)


        return ViewHolder2(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item.findViewById<TextView>(R.id.user_name_text).text = myDataset[position]

        holder.item.findViewById<ImageView>(R.id.user_avatar_image)
            .setImageResource(listOfAvatars[position % listOfAvatars.size])

        holder.item.setOnClickListener {// 각 친구들 목록 클릭시
            val bundle = bundleOf(USERNAME_KEY to myDataset[position]) // 데이터를 넘겨주기위해 저장

            holder.item.findNavController().navigate(
                R.id.action_friendListScreen_to_friendProfileScreen,
                bundle)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    companion object {
        const val USERNAME_KEY = "userName"
    }
}

private val listOfAvatars = listOf(
    R.drawable.avatar_1_raster,
    R.drawable.avatar_2_raster,
    R.drawable.avatar_3_raster,
    R.drawable.avatar_4_raster,
    R.drawable.avatar_5_raster,
    R.drawable.avatar_6_raster
)
