package com.besthora.moneytoringapp.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.besthora.moneytoringapp.databinding.ActivityProfileBinding
import com.besthora.moneytoringapp.helper.Moveactivity
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject

class ProfileActivity : AppCompatActivity() {
    private var navbottom: MeowBottomNavigation? = null
    private lateinit var binding : ActivityProfileBinding
    private var moveactv = Moveactivity()
    private val preferencesMoney: PreferencesMoney by inject()
    private lateinit var materialBtn : MaterialButton
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navbottom = binding.meowbottom
        navbottom!!.add(MeowBottomNavigation.Model(1, R.drawable.ic_home2))
        navbottom!!.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_format_list_bulleted_24))
        navbottom!!.add(MeowBottomNavigation.Model(3, R.drawable.ic_add))
        navbottom!!.add(MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person_outline_24))
        navbottom!!.show(4)

        navbottom!!.setOnShowListener { item: MeowBottomNavigation.Model ->
            when(item.id){
                1 -> moveactv.To(this, "HomeActivity")
                2 -> moveactv.To(this, "AllMoneyActivity")
                3 -> moveactv.To(this, "CreateTrxActivity")
            }
        }
        materialBtn = binding.showSettingPp
        materialBtn.setOnClickListener { showDialogOptionProfile() }

        if (preferencesMoney.permissionnotif == "accept"){
            binding.iconNotif.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_notifications_active_24))
            binding.statusPermissionNotif.text = "Akses Notifikasi diizinkan"
            binding.connectNotif.setText("BERHENTI IZINKAN AKSES")
            binding.connectNotif.setOnClickListener {
                preferencesMoney.permissionnotif = "decline"
                startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        }else{
            binding.iconNotif.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_notifications_off_24))
            binding.statusPermissionNotif.text = "Menolak Akses Notifikasi"
            binding.connectNotif.setText("IZINKAN NOTIFIKASI")
            binding.connectNotif.setOnClickListener {
                preferencesMoney.permissionnotif = "accept"
                startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        }
    }

    private fun showDialogOptionProfile() {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.show_option_profile)


        val showintentcam: Chip = dialog.findViewById(R.id.chip_update_ppimg)
        val logout: Chip = dialog.findViewById(R.id.chip_logout)

        showintentcam.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                dialog.cancel()
            }
        })
        logout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                preferencesMoney.relog_guest = preferencesMoney.getmode
                preferencesMoney.getmode = null
                goLoginlanding()
            }
        })
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

    private fun goLoginlanding() {
        moveactv.To(this, "LoginLanding")
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0);
    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveactv.To(this, "${intent.getStringExtra("ACT_NAME")}")
    }
}