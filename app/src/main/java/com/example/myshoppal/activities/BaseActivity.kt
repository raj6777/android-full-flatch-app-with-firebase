package com.example.myshoppal.activities

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.utils.MSPTextView
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mprogressdialoge:Dialog


        fun showErrorSnackBar(message:String,errorMessage:Boolean){
            val snackbar=
                Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
            val snackBarView=snackbar.view

            if(errorMessage){
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError)
                )
            }
            else{
                snackBarView.setBackgroundColor(ContextCompat.getColor(
                    this@BaseActivity,R.color.colorSnackBarSucess
                ))
            }
            snackbar.show()
        }
    fun showprogresDialog(text:String){

            mprogressdialoge = Dialog(this)
            mprogressdialoge.setContentView(R.layout.dialog_progress)
            mprogressdialoge.findViewById<MSPTextView>(R.id.tv_progress_text).text = text
            mprogressdialoge.setCancelable(false)
            mprogressdialoge.setCanceledOnTouchOutside(false)
            mprogressdialoge.show()
    }
    fun hideprogressDialog(){
        mprogressdialoge.dismiss()
    }
    }