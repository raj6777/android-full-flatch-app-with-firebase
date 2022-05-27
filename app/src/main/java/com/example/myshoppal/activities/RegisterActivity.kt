package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatCheckBox
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStore
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.MSPEditText
import com.example.myshoppal.utils.MSPTextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity() {
    lateinit var tool: androidx.appcompat.widget.Toolbar
    private lateinit var auth: FirebaseAuth
    lateinit var firstnameedittext: MSPEditText
    lateinit var secondnameedittext: MSPEditText
    lateinit var Remailedittext: MSPEditText
    lateinit var Rpassworddittext: MSPEditText
    lateinit var cpassworddittext: MSPEditText
    lateinit var checkcondition: AppCompatCheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        firstnameedittext = findViewById(R.id.etb_firstname)
        secondnameedittext = findViewById(R.id.et_secondname)
        Remailedittext = findViewById(R.id.et_R_email)
        Rpassworddittext = findViewById(R.id.et_R_password)
        cpassworddittext = findViewById(R.id.et_c_password)
        checkcondition = findViewById(R.id.cb_terms_and_condition)
        tool = findViewById(R.id.toolbar_register_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars());
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()
        val textviewbuttonforlogin: MSPTextViewBold = findViewById(R.id.tv_login)

        textviewbuttonforlogin.setOnClickListener {
            onBackPressed()
        }
        val button: Button = findViewById(R.id.btn_Register)
        button.setOnClickListener { registerUser() }
    }


    private fun setupActionBar() {
        setSupportActionBar(tool)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        tool.setNavigationOnClickListener { onBackPressed() }
    }

    private fun ValidateRegisterDetail(): Boolean {

        return when {
            TextUtils.isEmpty(firstnameedittext.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(secondnameedittext.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_second_name), true)
                false
            }
            TextUtils.isEmpty(Remailedittext.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(Rpassworddittext.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(cpassworddittext.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirmpassword), true)
                false
            }
            Rpassworddittext.text.toString().trim { it <= ' ' } != cpassworddittext.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirmpassword_mismatch),
                    true
                )
                false
            }

            !checkcondition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }

            else -> {
                //0 showErrorSnackBar(resources.getString(R.string.registry_successfully),false)
                true

            }
        }
    }

    private fun registerUser() {
        val Remailedittext: MSPEditText = findViewById(R.id.et_R_email)
        val Rpassworddittext: MSPEditText = findViewById(R.id.et_R_password)
        if (ValidateRegisterDetail()) {

            showprogresDialog(resources.getString((R.string.please_wait)))

            val email: String = Remailedittext.text.toString().trim { it <= ' ' }
            val password: String = Rpassworddittext.text.toString().trim { it <= ' ' }

            //create a instance and create a regidter a user with email and password

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                      // hideprogressDialog()

                        //if register is done sucessfully
                        if (task.isSuccessful) {
                            //Firebase register user

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                firstnameedittext.text.toString().trim{ it <= ' '},
                                secondnameedittext.text.toString().trim{it <= ' '},
                                Remailedittext.text.toString().trim{it <= ' ' }
                            )
                            FireStore().registerUser(this@RegisterActivity, user)
//
//                            showErrorSnackBar(
//                                "You are register successfully. your user id is ${firebaseUser.uid}",
//                                false
//                            )

//                            FirebaseAuth.getInstance().signOut()
//                            finish()

                        } else {
                            hideprogressDialog()

                            //If register is not successfully then show error message
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )

        }

    }
    fun userregistrationSucess(){
        hideprogressDialog()

        Toast.makeText(this@RegisterActivity,
        resources.getString(R.string.register_success),
        Toast.LENGTH_SHORT
        ).show()

    }


}