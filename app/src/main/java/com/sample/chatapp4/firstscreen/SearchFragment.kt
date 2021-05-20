package com.sample.chatapp4.firstscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.chatapp4.AdapterClasses.UserAdapter
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment:Fragment() { // 사용자 찾는 Fragment
    private  var userAdapter: UserAdapter?= null
    private var mUsers: List<Users>? = null
    private var recyclerView : RecyclerView? = null
    private var searchEditText : EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_search,container,false)
        Log.d("test","SearchFragmet 진입")

        recyclerView = view.findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        searchEditText = view.findViewById(R.id.searchUsersET)

        mUsers= ArrayList()
        retrieveAllUsers()

        searchEditText!!.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(s.toString().toLowerCase())
            }
            override fun afterTextChanged(s: Editable?) {

            }
        }  )


        return view
    }

    private fun retrieveAllUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users") // get all users from database

        refUsers.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList<Users>).clear()
                  if(searchEditText!!.text.toString()=="")
                  {
                      for(snapshot in p0.children)
                      {
                          val user: Users? = p0.getValue(Users::class.java)
                          if(!(user!!.getUID()).equals(firebaseUserID)) // except me
                          {
                              (mUsers as ArrayList<Users>).add(user)
                          }
                      }
                      Log.d("error", "mUsers"+mUsers.toString())

                      userAdapter = UserAdapter(context!!,mUsers!!,false)
                      Log.d("test","userAdapter까지 완료")
                      recyclerView!!.adapter = userAdapter
                  }


            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun searchForUsers(str: String){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers = FirebaseDatabase
            .getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/")
            .reference.child("Users").orderByChild("search")
            .startAt(str)
            .endAt(str+"\uf8ff")

        queryUsers.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(snapshot in p0.children)
                {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if(!(user!!.getUID()).equals(firebaseUserID)) // except me
                    {
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                Log.d("error", "mUsers"+mUsers.toString())
                userAdapter = UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }// end of fun searchForUsers
}// end of class SearchFragment