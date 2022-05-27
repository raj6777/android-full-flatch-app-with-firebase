package com.example.myshoppal.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myshoppal.R
import com.example.myshoppal.utils.Constants

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreferences=
            getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE )
        val username=sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!
        val main:TextView=findViewById(R.id.tv_main)
        main.text="Hello  $username."
    }
}