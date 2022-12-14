package com.example.sikjipsa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sikjipsa.model.ContentDTO
import com.example.sikjipsa.navigation.DetailViewFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class CommentActivity : AppCompatActivity(){

    private lateinit var nickname: String


    var contentUid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        contentUid = intent.getStringExtra("contentUid")
        FirebaseDatabase.getInstance().reference.child("user").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("nickname").get().addOnSuccessListener {
            nickname = it.value.toString()
        }
        // Connecting RecyclerView to Adapter, LaoutManager
        comment_recyclerview.adapter = CommentRecyclerviewAdapter()
        comment_recyclerview.layoutManager = LinearLayoutManager(this)

        // If user press the comment send button, data enters ContentDTO
        comment_btn_send?.setOnClickListener {

            var comment = ContentDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = comment_edit_message.text.toString()
            comment.timestamp = System.currentTimeMillis()
            comment.nickname = nickname

            // To accumulate comment message data in firestore
            FirebaseFirestore.getInstance().collection("images").document(contentUid!!).collection("comments").document().set(comment)

            comment_edit_message.setText("")

        }
    }

    // Screen to print out
    inner class CommentRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        // Initializing and bring data from firestore
        var contents : ArrayList<ContentDTO.Comment> = arrayListOf()
        init{
            FirebaseFirestore.getInstance()
                .collection("images")
                .document(contentUid!!)
                .collection("comments")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // Read values (remove duplicates)
                    contents.clear()
                    if(querySnapshot == null)return@addSnapshotListener

                    for(snapshot in querySnapshot.documents!!){
                        contents.add(snapshot.toObject(ContentDTO.Comment::class.java)!!)
                    }
                    // Refresh
                    notifyDataSetChanged()
                }
        }


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_comment,p0,false)
            return CustomViewHolder(view)
        }

        private inner class CustomViewHolder(view:View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return contents.size
        }
        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var view = p0.itemView
            view.commentviewitem_textview_comment.text = contents[p1].comment
            view.commentviewitem_textview_profile.text = contents[p1].nickname

            // ProfileImage
            val fs = FirebaseStorage.getInstance()
            fs.getReference().child("profilepic").child(contents[p1].uid.toString()).downloadUrl.addOnSuccessListener {
                var imageUrl = it
                Glide.with(p0.itemView.context).load(imageUrl).apply(RequestOptions().circleCrop()).into(view.commentviewitem_imageview_profile)
            }


        }


    }


}


