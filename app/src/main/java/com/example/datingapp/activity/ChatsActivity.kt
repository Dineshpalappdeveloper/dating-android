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

       //  getData(intent.getStringExtra("chat_id"))
        verifyChatId()

        binding.sendMessage.setOnClickListener {
            if (binding.typing.text.isEmpty()) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            } else {
                storeData(binding.typing.text.toString())
            }
        }


    }
    private var senderId :String? = null
    private var chatId :String? = null
    private fun verifyChatId() {
        val receiverId = intent.getStringExtra("userId")
        senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

         chatId = senderId + receiverId
        val reverseChatId = receiverId+senderId


        val reference =   FirebaseDatabase.getInstance().getReference("chats")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(chatId!!)){
                    getData(chatId)
                }else if (snapshot.hasChild(reverseChatId)){
                   chatId = reverseChatId
                    getData(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatsActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getData(chatId: String?) {

        System.out.println("chatId: " + chatId);


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



    private fun storeData( msg: String) {
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate
        val reference =   FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)
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