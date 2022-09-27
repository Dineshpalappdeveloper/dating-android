package com.example.datingapp.Util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.datingapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder



object Config {
  private  var dialogo : AlertDialog? = null

    fun showDialog(context: Context){
     //dialogo = AlertDialog.Builder(this)
        dialogo = MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        dialogo!!.show()
    }
    fun hideDialog(){
        dialogo!!.dismiss()
    }
}