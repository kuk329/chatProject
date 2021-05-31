package com.sample.chatapp4.AdapterClasses

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.chatapp4.LoginActivity
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.sample.chatapp4.second.MessageChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_friend_list.view.*

class UserAdapter(
    mContext: Context,
    mUsers: ArrayList<Users>,
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
        Log.d("adapter","초기화")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.list_friend_item,parent,false)
        Log.d("adapter","onCreateViewHolder")

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("adapter","getItemCount")
        return mUsers.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val user: Users = mUsers[position]
        holder.userNameTxt.text=  mUsers[position]!!.getUserName()

        //   Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_img).into(holder.profileImageView)


        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "메세지 보내기",
                "프로필 보기"
            )
            val builder : AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setTitle("What do you want?")
            builder.setItems(options,DialogInterface.OnClickListener{dialog, position ->
                if(position == 0){
                    val intent = Intent(mContext, MessageChatActivity::class.java)
                    intent.putExtra("visit_id",user.getUID())
                    mContext.startActivity(intent)
                }
                if(position == 1){

                    // visit profile

                }
            })

            builder.show()
        }

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {

        var userNameTxt: TextView
        var profileImageView : CircleImageView
        var onlineImageView: CircleImageView
        var offlineImageView: CircleImageView
        // var lastMessageTxt: TextView   // 최근 메세지 내용 <- 메세지 리스트 adapter에 추가

        init{
            userNameTxt = itemView.findViewById(R.id.username)
            profileImageView = itemView.findViewById(R.id.profile_image)
            onlineImageView = itemView.findViewById(R.id.image_online)
            offlineImageView = itemView.findViewById(R.id.image_offline)
            //    lastMessageTxt = itemView.findViewById(R.id.message_last)
        }
    } // end of class ViewHolder

}// end of class UserAdpater










