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

package com.sample.chatapp4.thirdscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sample.chatapp4.R
import com.sample.chatapp4.WelcomeActivity


/**
 * Shows a register third to showcase UI state persistence. It has a button that goes to [Registered]
 */
class Register : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

//        view.findViewById<Button>(R.id.goto_profile_btn).setOnClickListener {
//            val intent = Intent(activity,SettingProfileFragment::class.java)
//            startActivity(intent)
//        }
//        view.findViewById<Button>(R.id.logout_btn).setOnClickListener {
//      //      findNavController().navigate(R.id.action_register_to_registered)
//            FirebaseAuth.getInstance().signOut()
//            val intent = Intent(activity, WelcomeActivity::class.java)
//            // 액티비티 프레임에서 제거 (BACK버튼 눌렀을때 되돌아 오지 않도록)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//
//        }
        return view
    }
}
