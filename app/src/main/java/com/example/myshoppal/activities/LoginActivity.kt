package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStore
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.MSPButton
import com.example.myshoppal.utils.MSPEditText
import com.example.myshoppal.utils.MSPTextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : BaseActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars());
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

        }
        val textviewbutton: MSPTextViewBold = findViewById(R.id.tv_register)

        textviewbutton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val forgotpasasword:MSPTextViewBold=findViewById(R.id.tv_forgot_password)
        forgotpasasword.setOnClickListener(this)
        val loginbutton:MSPButton=findViewById(R.id.btn_login)
        loginbutton.setOnClickListener(this)

       }
    fun userLoggedInSuccess(user:User){
        //Hide the progress dialog
        hideprogressDialog()
        //print the user details in the log as of now
        Log.i("First Name",user.firstname)
        Log.i("Last Name",user.lastname)
        Log.i("Email",user.email)

        if(user.profilecompleted == 0){
            val intent=Intent(this@LoginActivity,UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
        finish()

    }

    override fun onClick(view: View?){

        if(view!=null){
            when(view.id){
                R.id.tv_forgot_password->{
                    val intent = Intent(this@LoginActivity,ForgotPassword::class.java)
                    startActivity(intent)

                }
                R.id.btn_login->{
                   loginregisteruser()
                }
                R.id.tv_register->{
                    val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
    fun validateLoginDetail(): Boolean {
        val errmsgemail: MSPEditText =findViewById(R.id.et_email)
        val errmsgpassword: MSPEditText =findViewById(R.id.et_password)
        return when{
            TextUtils.isEmpty(errmsgemail.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(errmsgpassword.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else->{
               // showErrorSnackBar("Your detail are valid",false)
                true

            }
        }
    }
    private fun loginregisteruser(){
        val lemailedittext: MSPEditText=findViewById(R.id.et_email)
        val lpassworddittext: MSPEditText=findViewById(R.id.et_password)
        if(validateLoginDetail()){

            showprogresDialog(resources.getString((R.string.please_wait)))

            val email:String=lemailedittext.text.toString().trim { it <= ' '}
            val password:String=lpassworddittext.text.toString().trim { it <= ' '}

            //create a instance and create a regidter a user with email and password

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        hideprogressDialog()

                        //id login is done sucessfully
                        if(task.isSuccessful){
                            //Firebase login user
                            FireStore().getUserDetails(this@LoginActivity)
//                            FirebaseAuth.getInstance().signOut()
//                            finish()

                        }else{
                            hideprogressDialog()

                            //If register is not successfully then show error message
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
                )

        }

    }


}