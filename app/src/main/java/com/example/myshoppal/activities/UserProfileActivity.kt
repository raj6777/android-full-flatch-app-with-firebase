package com.example.myshoppal.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStore
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit  var muserDetail: User
    private var load: ActivityResultLauncher<String>? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            muserDetail=intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        etb_firstname.isEnabled = false
        etb_firstname.setText(muserDetail.firstname)

        etb_lastname.isEnabled = false
        etb_lastname.setText(muserDetail.lastname)

        et_P_email.isEnabled = false

        et_P_email.setText(muserDetail.email)
        load = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            {
               GlideLoader(this).loadUserPicture(it,iv_user_photo)
            })

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_Submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_user_photo->{
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED) {
                        //showErrorSnackBar("You already have storage permission",false)
//                         Constants.showImageChooser(this)
                        load?.launch("image/*")
                    }else {
                          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                          Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.btn_Submit->{
                    if(validateUserProfileDetail()){
                        val userHashMap = HashMap<String, Any>()
                        val mobilenumber=et_mobileno.text.toString().trim{it <= ' '}

                        val gender=
                            if(rb_male.isChecked){
                            Constants.MALE
                        }
                        else{
                            Constants.FEMALE
                        }
                        if(mobilenumber.isNotEmpty()){
                            userHashMap[Constants.MOBILE]=mobilenumber.toLong()
                        }
                        userHashMap[Constants.GENDER]=gender

                        showprogresDialog(resources.getString(R.string.please_wait))

                        FireStore().updateUserProfileData(this,userHashMap)

                        //showErrorSnackBar("Your detail are ready you can update them",false)
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            Log.d("onreqPer", "Its working")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("The storage permission is granted", false)
//                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this, resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.e("request",requestCode.toString())
//        Log.e("result",resultCode.toString())
//
//        Log.e("data", data?.data.toString())
//
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
//               Log.d("inside both if ",requestCode.toString())
//                if(data!=null){
//                    try{
//                        val selectedImageFileUri=data.data!!
//                        iv_user_photo.setImageURI(selectedImageFileUri)
//                        Log.d("hey",selectedImageFileUri.toString())
//                    }catch (e:IOException){
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@UserProfileActivity,
//                            resources.getString(R.string.image_selection_failed),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                       }
//                }
//            }
//        }
//    }

    private fun validateUserProfileDetail():Boolean{
        return when{
            TextUtils.isEmpty(et_mobileno.text.toString().trim{it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number),true)
                false
            }
            else->{
                true
            }
        }
    }
}