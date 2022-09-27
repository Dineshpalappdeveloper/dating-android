package com.example.datingapp.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityLoginBinding

    val auth =FirebaseAuth.getInstance()
    private var verificationId: String? = null

    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        binding.SendOTP.setOnClickListener {
         if (binding.UserNumber.text!!.isEmpty()){
             binding.UserNumber.error = "Please enter your number"
         }else{

             sendOtp(binding.UserNumber.text.toString())
         }
        }
        binding.VerifyOtp.setOnClickListener {
            if (binding.UserOtp.text!!.isEmpty()){
                binding.UserOtp.error = "Please enter your OTP"
            }else{
                verifyOtp(binding.UserOtp.text.toString())
            }
        }
    }

    private fun verifyOtp(Otp: String) {
            dialog.show()
    //    binding.SendOTP.showLoadingButton()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, Otp)

        signInWithPhoneAuthCredential(credential)

    }

    private fun sendOtp(number: String) {
     //   binding.SendOTP.showLoadingButton()
        dialog.show()
      val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      //          binding.SendOTP.showNormalButton()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            )
            {
                dialog.dismiss()
                   this@LoginActivity.verificationId = verificationId

           //         binding.SendOTP.showNormalButton()
                    binding.NumberLayout.visibility = GONE
                    binding.OtpLayout.visibility = VISIBLE

            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")       // Phone number to verify

            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
          //      binding.SendOTP.showNormalButton()
                if (task.isSuccessful) {

                    checkUserExist(binding.UserNumber.text.toString())
              //    startActivity(Intent(this,MainActivity::class.java))
                  //  finish()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this,"Wrong OTP", Toast.LENGTH_SHORT).show()
                    }

                }
            }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91$number")
            .addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError){
                dialog.dismiss()
                Toast.makeText(this@LoginActivity,p0.message, Toast.LENGTH_SHORT).show()
            }
                override fun onDataChange(p0: DataSnapshot){
                    if(p0.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                         finish()
                    }else{
                        startActivity(Intent(this@LoginActivity,RegistionActivity::class.java))
                          finish()
                    }
                }
            })

    }
}
