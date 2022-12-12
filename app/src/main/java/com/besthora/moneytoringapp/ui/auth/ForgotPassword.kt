package com.besthora.moneytoringapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.besthora.moneytoringapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val submitButton: Button = findViewById(R.id.forgotPassBtn)
        val etEmail: EditText = findViewById(R.id.emailForgotPass)

        submitButton.setOnClickListener {
            val email: String = etEmail.text.toString().trim { it <= ' ' }
            if (email.isEmpty()){
                Toast.makeText(this, "Mohon masukkan alamat email anda", Toast.LENGTH_LONG).show()

            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "cek Email Anda", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}