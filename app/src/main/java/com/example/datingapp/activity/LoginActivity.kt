package com.example.datingapp.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    val auth =FirebaseAuth.getInstance()
    private var verificationId: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    //    binding.SendOTP.showLoadingButton()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, Otp)

        signInWithPhoneAuthCredential(credential)

    }

    private fun sendOtp(number: String) {
     //   binding.SendOTP.showLoadingButton()
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
            ) {
                   this@LoginActivity.verificationId = verificationId

           //         binding.SendOTP.showNormalButton()
                    binding.NumberLayout.visibility = GONE
                    binding.OtpLayout.visibility = VISIBLE

            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91")       // Phone number to verify

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
                  startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                   // Toast.makeText(this,task.exception.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
    }
