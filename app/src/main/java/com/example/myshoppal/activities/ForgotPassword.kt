package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.utils.MSPButton
import com.example.myshoppal.utils.MSPEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : BaseActivity() {
    lateinit var forgottool: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        forgottool = findViewById(R.id.toolbar_forgot_password_activity);

        setupActionBar()
    }

    private fun setupActionBar() {
        val btn_submit: MSPButton = findViewById(R.id.btn_submit)
        setSupportActionBar(forgottool)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        forgottool.setNavigationOnClickListener { onBackPressed() }
        btn_submit.setOnClickListener {
            val forgotpasswordemail: MSPEditText = findViewById(R.id.forgot_et_email)
            val email: String = forgotpasswordemail.text.toString().trim { it <= ' ' }

            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showprogresDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideprogressDialog()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPassword,
                                resources.getString(R.string.email_send_sucess),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}