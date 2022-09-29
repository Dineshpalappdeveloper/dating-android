package com.example.datingapp.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.datingapp.R
import com.example.datingapp.Ui.DatingFragment.Companion.list
import com.example.datingapp.Util.Config
import com.example.datingapp.adapter.MessageUserAdapter
import com.example.datingapp.databinding.FragmentMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
    }

    private fun getData() {
        Config.showDialog(requireContext())

        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = arrayListOf<String>()
                    var newList = arrayListOf<String>()
                    for (data in snapshot.children) {
                        if (data.key!!.contains(currentId!!)) {
                            list.add(data.key!!.replace(currentId!!, ""))
                            newList.add((data.key!!))
                        }
                    }
                    try {


                        binding.recycleView.adapter =
                            MessageUserAdapter(requireContext(), list, newList)
                    } catch (e: Exception) {

                    }


                    Config.hideDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "error.message", Toast.LENGTH_SHORT).show()
                }

            })
    }

}