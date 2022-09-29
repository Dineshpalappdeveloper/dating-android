package com.example.datingapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.Util.Config
import com.example.datingapp.Util.Config.hideDialog
import com.example.datingapp.databinding.ActivityRegistionBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage



class RegistionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistionBinding
    private var imageUri : Uri? = null
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
       imageUri = it
       binding.UserImage.setImageURI(imageUri)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.UserImage.setOnClickListener {
            selectImage.launch("image/*")
        }
        binding.btnNext.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.UserName.text.toString().isEmpty()
            || binding.UserEmail.text.toString().isEmpty()
            || binding.UserCity.text.toString().isEmpty()
            || imageUri == null){
            Toast.makeText(this,"Please enter all fields ", Toast.LENGTH_SHORT).show()
        }else if (!binding.TermsCondition.isChecked){
            Toast.makeText(this,"Please accept terms and conditions", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

     val storageRef =  FirebaseStorage.getInstance().getReference("profile")
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeData(imageUrl: Uri?) {
       
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
           val  token = task.result


            val data = UserModel(
                name = binding.UserName.text.toString(),
                image = imageUrl.toString(),
                email = binding.UserEmail.text.toString(),
                city = binding.UserCity.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber,
                fcmToken = token
            )
            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener{
                    hideDialog()
                    if (it.isSuccessful){
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                        Toast.makeText(this," register succesfully ", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,it.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }

        })


    }
}
