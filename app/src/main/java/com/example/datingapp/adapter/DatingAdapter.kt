package com.example.datingapp.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.Model.UserModel
import com.example.datingapp.activity.ChatsActivity
import com.example.datingapp.databinding.ItemUserLayoutBinding

class DatingAdapter(val context: Context,val list: ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context)
        , parent, false))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.UserProfileName.text = list[position].name
        holder.binding.UserProfileId.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.imageView)

        holder.binding.messageDetails.setOnClickListener {
            val mintent = Intent(context,ChatsActivity::class.java)
            mintent.putExtra("userId",list[position].number)
            context.startActivity(mintent)
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
}