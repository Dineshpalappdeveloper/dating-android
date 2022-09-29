package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.datingapp.activity.DeveloperActivity
import com.example.datingapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity() , OnNavigationItemSelectedListener  {
    private lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle : ActionBarDrawerToggle ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drawerLayout, R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.rateUs -> {
               Toast.makeText(this,"rateUs", Toast.LENGTH_SHORT).show()
           }
           R.id.shareApp -> {
               Toast.makeText(this,"ShareApp", Toast.LENGTH_SHORT).show()
           }
           R.id.TermsCondition -> {
               Toast.makeText(this,"Terms And Conditions", Toast.LENGTH_SHORT).show()
           }
           R.id.favourite -> {
               Toast.makeText(this,"Favourite", Toast.LENGTH_SHORT).show()
           }
           R.id.aboutDeveloper -> {
               Toast.makeText(this,"Welcome", Toast.LENGTH_SHORT).show()
               startActivity(Intent(this,DeveloperActivity::class.java))
           }
       }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }else

        super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }else
            super.onBackPressed()
    }

}
// messageFragment line 50 adapter messageUserAdapter
// messageAdapter  error