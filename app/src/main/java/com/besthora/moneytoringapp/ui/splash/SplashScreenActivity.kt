package com.besthora.moneytoringapp.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.ui.auth.LoginLandingActivity
import com.besthora.moneytoringapp.ui.main.HomeActivity
import org.koin.android.ext.android.inject

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 3000
    private val preferencesMoney: PreferencesMoney by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreference =  getSharedPreferences("MODE_GUEST", Context.MODE_PRIVATE)
        val idguest = sharedPreference.getString("id_guest", null)

        Handler().postDelayed({
            startActivity(Intent(this,
                if (preferencesMoney.getmode == null) LoginLandingActivity::class.java
                else HomeActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}