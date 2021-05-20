package com.sample.chatapp4.AdapterClasses

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_friend_list.view.*

class UserAdapter(
    mContext: Context,
    mUsers: List<Users>,
    isChatCheck : Boolean
    ) : RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{
    private val mContext:Context
    private val mUsers : List<Users>
    private var isChatCheck: Boolean

    init{
        this.mUsers= mUsers
        this.mContext =mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users = mUsers[position]
        Log.d("test33","mUser: "+mUsers.toString())
//        Log.d("test33","getProfile: "+user.getProfile())
//        Log.d("test33","getUserName: "+user.getUserName())
//        Log.d("test33","getCover: "+user.getCover())
//        Log.d("test33","getFacebook: "+user.getFacebook())
//        Log.d("test33","getInstagram: "+user.getInstagram())

        holder.userNameTxt.text=  mUsers[position]!!.getUserName()

        Log.d("test33","이름:"+ mUsers[position].getUserName())
        Log.d("test33","이름:"+ mUsers[position].getProfile())

        Log.d("test33","위치: "+position)
        Log.d("test33","tsd"+mUsers[0])
        Log.d("test33","tsd"+mUsers[1])
        Log.d("test33","tsd"+mUsers[2])
        Log.d("test33","last!!"+user.toString())
        Log.d("test33","last!!"+user.getProfile())
        Log.d("test33","last!!"+user.getUserName())
        Log.d("test33","last!!"+user.getInstagram())



        Log.d("test22","user.getPorfile()"+ mUsers[position].getProfile())
        //Picasso.get().load( mUsers[position].getProfile()).placeholder(R.drawable.profile_img).into(holder.profileImageView)
        //Log.d("test22","사진 url:"+text)

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        // 변수 선언
        var userNameTxt: TextView
        var profileImageView : CircleImageView
        var onlineImageView: CircleImageView
        var offlineImageView: CircleImageView
        var lastMessageTxt: TextView   // 최근 메세지 내용

        // 연결 (초기화)
        init{
            userNameTxt = itemView.findViewById(R.id.username)
            profileImageView = itemView.findViewById(R.id.profile_image)
            onlineImageView = itemView.findViewById(R.id.image_online)
            offlineImageView = itemView.findViewById(R.id.image_offline)
            lastMessageTxt = itemView.findViewById(R.id.message_last)
        }
    } // end of class ViewHolder

}// end of class UserAdpater











