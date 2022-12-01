package com.example.sikjipsa.navigation

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.inline.InlineContentView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sikjipsa.*
import com.example.sikjipsa.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import kotlinx.android.synthetic.main.item_detail.view.*
import java.lang.System.load

class UserFragment : Fragment(){
    //수오 유저 로그아웃
    var fragmentView: View? = null
    var firestore: FirebaseFirestore? = null
    var uid : String? = null
    var auth: FirebaseAuth? = null
    var currentUserId: String? = null
    var mDBRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var photoUri: Uri? = null



    private var editBtn: Button? = null
    private var watering: Button? = null
    private var mypostBtn: Button? = null
    private var name: String? = null
    private var editpic: Button? = null
    private var editnickname: Button? = null
    private var edittextnickname:TextView? = null
    private var donebtn: Button? = null
//    private var profile: ImageView? = null



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
        uid = auth?.currentUser?.uid

        mDBRef = FirebaseDatabase.getInstance().reference
        var nickname : String? = null
        //        닉네임 정보 받아옴 -> jigglypuff설정필요
        mDBRef.child("user").child(uid.toString()).child("nickname").get().addOnSuccessListener {
            nickname = it.value.toString()
            Log.d("nickname","$nickname")
            fragmentView?.name?.text = nickname
        }

        //if(uid == currentUserId){
            //내 계정일 때는 로그아웃
            fragmentView?.button_signout?.setOnClickListener{
                auth?.signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        //}
        
//        프로필편집 화면 전환
        name = fragmentView?.findViewById<TextView>(R.id.name).toString()
//        profile = fragmentView?.findViewById<ImageView>(R.id.profile)
        editBtn = fragmentView?.findViewById(R.id.editBtn)
        watering = fragmentView?.findViewById(R.id.wateringBtn)
        mypostBtn = fragmentView?.findViewById(R.id.mypostBtn)
        editpic = fragmentView?.findViewById(R.id.editpic)
        editnickname = fragmentView?.findViewById(R.id.editnickname)
        edittextnickname = fragmentView?.findViewById(R.id.edittextnickname)
        donebtn = fragmentView?.findViewById(R.id.donebtn)

        //ProfileImage
        val fs = FirebaseStorage.getInstance()



        fs.getReference().child("profilepic").child(uid.toString()).downloadUrl.addOnSuccessListener { it ->
            var imageUrl = it
            Glide.with(this).load(imageUrl).into(profile)
        }



        editBtn?.setOnClickListener {
//            startActivity(Intent(context, EditProfileActivity::class.java))
            editBtn?.visibility = GONE
            editpic?.visibility = VISIBLE
            editnickname?.visibility = VISIBLE

            editnickname?.setOnClickListener {
                editnickname?.visibility = GONE
                edittextnickname?.visibility = VISIBLE
                donebtn?.visibility = VISIBLE
            }

            donebtn?.setOnClickListener {
                val newnick:String = edittextnickname?.text.toString()
                mDBRef.child("user/${uid.toString()}/nickname").setValue("$newnick")
                var nickname : String? = null

                mDBRef.child("user").child(uid.toString()).child("nickname").get().addOnSuccessListener {
                    nickname = it.value.toString()
                    Log.d("nickname","$nickname")
                    fragmentView?.name?.text = nickname
                }
            }

            editpic?.setOnClickListener {

            }

            donebtn?.setOnClickListener {

            }



        }








        editnickname?.setOnClickListener {


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