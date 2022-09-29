package com.example.datingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityDeveloperBinding
import com.example.datingapp.databinding.ActivityMainBinding

class DeveloperActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeveloperBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}