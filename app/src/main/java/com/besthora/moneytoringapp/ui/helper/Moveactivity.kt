package com.besthora.moneytoringapp.helper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.besthora.moneytoringapp.list.AllMoneyActivity
import com.besthora.moneytoringapp.ui.auth.LoginActivity
import com.besthora.moneytoringapp.ui.auth.LoginLandingActivity
import com.besthora.moneytoringapp.ui.auth.RegisterActivity
import com.besthora.moneytoringapp.ui.create.CreateTrxActivity
import com.besthora.moneytoringapp.ui.main.HomeActivity
import com.besthora.moneytoringapp.ui.profile.ProfileActivity
import kotlinx.datetime.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class Moveactivity: AppCompatActivity(){

    fun To(context: Context, actname : String){
        when(actname){
            "HomeActivity" -> intent = Intent(context, HomeActivity::class.java)
            "AllMoneyActivity" -> intent = Intent(context, AllMoneyActivity::class.java)
            "CreateTrxActivity" -> intent = Intent(context, CreateTrxActivity::class.java)
            "ProfileActivity" -> intent = Intent(context, ProfileActivity::class.java)
            "tmp_login" -> intent = Intent(context, LoginActivity::class.java)
            "tmp_reg" -> intent = Intent(context, RegisterActivity::class.java)
            "mode_guest" -> intent = Intent(context, HomeActivity::class.java)
            "LoginLanding" -> intent = Intent(context, LoginLandingActivity::class.java)
        }

        if (actname == "mode_guest"){
            intent.putExtra("MODE_GUEST_EXTRA", "gueston");
        }

        intent.putExtra("ACT_NAME", context.javaClass.simpleName!!);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_TASK_ON_HOME and Intent.FLAG_ACTIVITY_NO_ANIMATION and Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        finish()

    }

}