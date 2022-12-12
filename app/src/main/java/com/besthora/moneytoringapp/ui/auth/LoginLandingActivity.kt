package com.besthora.moneytoringapp.ui.auth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.databinding.ActivityLoginLandingBinding
import com.besthora.moneytoringapp.helper.Moveactivity
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject

class LoginLandingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginLandingBinding
    private var moveactv = Moveactivity()
    private val preferencesMoney: PreferencesMoney by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toLoginPage.setOnClickListener {
            moveactv.To(this, "tmp_login")
        }
        binding.toRegisterPage.setOnClickListener{
            moveactv.To(this, "tmp_reg")
        }

        binding.toGuestMode.setOnClickListener{
            if (preferencesMoney.relog_guest!= null){
                showAlreadySession()
            }else{
                moveactv.To(this, "mode_guest")
            }
        }
    }


    private fun goHomeGuest(){
        moveactv.To(this, "mode_guest")
    }
    private fun showAlreadySession() {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.show_option_login_guest)


        val loginagain: Chip = dialog.findViewById(R.id.chip_loginagain)
        val newsession: Chip = dialog.findViewById(R.id.chip_newsession)

        loginagain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                preferencesMoney.getmode = preferencesMoney.relog_guest
                goHomeGuest()
            }
        })

        newsession.setOnClickListener {
            preferencesMoney.relog_guest = null
            preferencesMoney.getmode = null
            goHomeGuest()
        }


        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

    }
}