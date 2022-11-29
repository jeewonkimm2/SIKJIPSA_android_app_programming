package com.example.sikjipsa.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sikjipsa.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import org.w3c.dom.Text

class UserFragment : Fragment(){
    //수오 유저 로그아웃
    var fragmentView: View? = null
    var firestore: FirebaseFirestore? = null
    var uid : String? = null
    var auth: FirebaseAuth? = null
    var currentUserId: String? = null
    var mDBRef: DatabaseReference = FirebaseDatabase.getInstance().reference


    private var editBtn: Button? = null
    private var plantBtn: Button? = null
    private var watering: Button? = null
    private var mypostBtn: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //지훈 mypage view이름 수오가 fragmentView로 바꿈
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_user,container,false)
        //수오
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserId = auth?.currentUser?.uid

        //if(uid == currentUserId){
            //내 계정일 때는 로그아웃
            fragmentView?.button_signout?.setOnClickListener{
                auth?.signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        //}
        
//        프로필편집 화면 전환
        editBtn = fragmentView?.findViewById(R.id.editBtn)
        plantBtn = fragmentView?.findViewById(R.id.myplantBtn)
        watering = fragmentView?.findViewById(R.id.wateringBtn)
        mypostBtn = fragmentView?.findViewById(R.id.mypostBtn)

        Log.d("nickname","textview2 : ${textView2?.text}")


//        닉네임 정보 받아옴 -> jigglypuff설정필
        mDBRef.child("user").child(uid.toString()).child("nickname").get().addOnSuccessListener {
            it.value.toString()
        }



        editBtn?.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        plantBtn?.setOnClickListener{
            startActivity(Intent(context, myPlantActivity::class.java))
        }

        mypostBtn?.setOnClickListener {
            startActivity(Intent(context, MyPostActivity::class.java))
        }

        watering?.setOnClickListener{
            startActivity(Intent(context, WateringActivity::class.java))
        }

        return fragmentView


    }
}