package com.example.myshoppal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import java.io.IOException

class GlideLoader(val context:Context){
    fun loadUserPicture(imageURI: Uri,imageView: ImageView){
        try{
            Glide.with(context)
                .load(imageURI)
                .centerCrop()
                .placeholder(R.drawable.userprofile)
                .into(imageView)
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}