package com.example.datingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.example.datingapp.Model.MessageModel
import com.example.datingapp.adapter.MessageAdapter

import com.example.datingapp.databinding.ActivityChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ChatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)


        setContentView(binding.root)

         getData(intent.getStringExtra("chat_id"))
        binding.sendMessage.setOnClickListener {
            if (binding.typing.text.isEmpty()) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(binding.typing.text.toString())
            }
        }


    }

    private fun getData(chatId: String?) {

        

            FirebaseDatabase.getInstance().getReference("chats")
                .child(chatId!!).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       val list = arrayListOf<MessageModel>()

                       for (show in snapshot.children){
                           list.add(show.getValue(MessageModel::class.java)!!)
                       }
                        binding.recyclerView2.adapter = MessageAdapter(this@ChatsActivity,list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatsActivity, "error.message", Toast.LENGTH_SHORT).show()
                    }

                })

    }

    private fun sendMessage(msg: String) {
        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId = senderId + receiverId
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val reference =   FirebaseDatabase.getInstance().getReference("chats").child(chatId)
        reference.child(reference.push().key!!)
            .setValue(map).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.typing.text = null
                    Toast.makeText(this, "Message send", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Message not send! Please send again", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}