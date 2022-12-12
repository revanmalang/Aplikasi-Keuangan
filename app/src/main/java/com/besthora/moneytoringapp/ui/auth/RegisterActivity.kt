package com.besthora.moneytoringapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.besthora.moneytoringapp.MainActivity
import com.besthora.moneytoringapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.requestSignup.setOnClickListener {

            val name = binding.inputname.text.toString()
            val email = binding.inputemail.text.toString()
            val pass = binding.inputpassword.text.toString()
            val confirmPass = binding.inputpasswordretype.text.toString()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(pass.isNotEmpty() && confirmPass.isNotEmpty()){
                    if (pass == confirmPass){
                        binding.progressBar.visibility = View.VISIBLE
                        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                            if (it.isSuccessful){
                                val intent = Intent(this, MainActivity::class.java)
                                Toast.makeText(this, "Sign Up Sukses", Toast.LENGTH_LONG).show()
                                binding.progressBar.visibility = View.GONE
                                startActivity(intent)
                            }else{
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(this, "Password tidak matching!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Pastikan semua terisi!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG).show()
            }
        }

        binding.toLoginPage.setOnClickListener {
            finish()
        }

    }
}