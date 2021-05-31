
package com.sample.chatapp4.thirdscreen

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.sample.chatapp4.ModelClasses.Users
import com.sample.chatapp4.R
import com.sample.chatapp4.WelcomeActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*


/**
 * Shows "Done".
 */
class SettingProfileFragment : Fragment() { // 내 계정 설정 페이지
    var userReference : DatabaseReference? =null
    var firebaeUser : FirebaseUser? = null
    private  val RequestCode = 438
    private var imageUri : Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = "" // 커버 이미지인지 프로필 이미지인지 확인

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_setting_profile, container, false)
        view.findViewById<Button>(R.id.logout_btn).setOnClickListener {
            //      findNavController().navigate(R.id.action_register_to_registered)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, WelcomeActivity::class.java)
            // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        firebaeUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance("https://messengerapp-45874-default-rtdb.firebaseio.com/").reference.child("Users").child(firebaeUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")
        userReference!!.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,"데이터 가져오기 실패",Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user : Users? = p0.getValue(Users::class.java)

                    if(context!=null){
                        view.username_setting.text = user!!.getUserName()
                        Picasso.get().load(user.getProfile()).into(view.profile_image_setting)
                        Picasso.get().load(user.getCover()).into(view.cover_image_setting)
                    }
                }
            }
        })

        view.profile_image_setting.setOnClickListener{
            pickImage()
        }
        view.cover_image_setting.setOnClickListener{
            coverChecker ="cover"
            pickImage()
        }



        return view
    }

    private fun pickImage()  // change image
    {
        val intent = Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RequestCode && resultCode== Activity.RESULT_OK && data!!.data!=null)
        {
            imageUri = data.data
            Toast.makeText(context,"이미지 업로드중...",Toast.LENGTH_LONG).show()
            uploadImageToDatabase()
        }
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("이미지가 업로드 중입니다. 기다려주세요")
        //progressBar.setTitle("")
        progressBar.show()

        if(imageUri!=null)
        {   // 이미지 이름 안겹치게
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString()+".jpg")

            var uploadTask : StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task->
                if(task.isSuccessful)
                {
                    task.exception?.let{
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if(coverChecker =="cover")
                    {
                        val mapCoverImg = HashMap<String,Any>()
                        mapCoverImg["cover"]  = url
                        userReference!!.updateChildren(mapCoverImg)
                        coverChecker = ""
                    }else
                    {
                        val mapProfileImg = HashMap<String,Any>()
                        mapProfileImg["profile"]  = url
                        userReference!!.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    progressBar.dismiss()
                }
            }


        }
    } // uploadImageToDatabase
}