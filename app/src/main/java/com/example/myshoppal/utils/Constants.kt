package com.example.myshoppal.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult


object Constants {
    const val USERS: String = "users"
    const val MYSHOPPAL_PREFERENCES: String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2

    const val  MALE:String="Male"
    const val FEMALE:String="Female"

    const val MOBILE:String="mobile"
    const val GENDER:String="gender"




//    fun showImageChooser(activity: Activity) {
//        val galleryIntent = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
//    }
//
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        activity.startActivityForResult(
//            Intent.createChooser(
//                intent,
//                "Select Picture"
//            ), PICK_IMAGE_REQUEST_CODE
//        )
    //}
}