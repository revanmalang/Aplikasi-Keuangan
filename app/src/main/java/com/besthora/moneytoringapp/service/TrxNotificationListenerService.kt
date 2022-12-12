package com.besthora.moneytoringapp.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.besthora.moneytoringapp.data.helper.InsertTrxHelper
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.data.repository.MoneyRepository
import com.besthora.moneytoringapp.service.TrxNotificationListenerService.ApplicationPackageNames.BRIMO_PKGNAME
import com.besthora.moneytoringapp.service.TrxNotificationListenerService.ApplicationPackageNames.DANA_PKGNAME
import com.besthora.moneytoringapp.service.TrxNotificationListenerService.ApplicationPackageNames.OVO_PKGNAME
import com.besthora.moneytoringapp.service.TrxNotificationListenerService.ApplicationPackageNames.SMS_PKGNAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*


class TrxNotificationListenerService: NotificationListenerService() {
    private val moneyRepository : MoneyRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val preferencesMoney: PreferencesMoney by inject()
    private lateinit var userId: String

    object ApplicationPackageNames {
        const val BRIMO_PKGNAME = "id.co.bri.brimo"
        const val OVO_PKGNAME = "ovo.id"
        const val DANA_PKGNAME = "id.dana"
        const val SMS_PKGNAME = "com.android.mms"
    }
    object InterceptedNotificationCode {
        const val BRIMO_CODE = 1
        const val OVO_CODE = 2
        const val DANA_CODE = 3
        const val SMS_CODE = 4
        const val OTHER_NOTIFICATIONS_CODE = 5
    }

    private var PreviousNotificationKey: String? = null
    private var savedKey : Int = 0
    private var savedTime : Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if(preferencesMoney.getmode != null) {
            userId = preferencesMoney.getmode.toString()
        }

        val ext = sbn.notification.extras

        val text = ext.getCharSequence("android.text").toString()
        PreviousNotificationKey = sbn.key
        PreviousNotificationKey = sbn.key
        savedKey = sbn.key.split("|")[sbn.key.split("|").lastIndex].toInt()
        savedTime = sbn.postTime.toInt()

        val sharedPreference =  getSharedPreferences("TEMP_NOTIF", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("text_tmp_title","${text}")
        editor.putInt("time_tmp",sbn.postTime.toInt())
        editor.commit()

        val tmpt = sharedPreference.getString("text_tmp_title","null")
        val tmptime = sharedPreference.getInt("time_tmp",1)



        val notificationCode: Int = matchNotificationCode(sbn)
        if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {

            val ext = sbn.notification.extras

            val text = ext.getCharSequence("android.text").toString()
            val title = ext.getCharSequence("android.title").toString()

            scope.launch {
                val insertTrxHelper =  InsertTrxHelper(title,
                    text,
                    sbn.packageName,
                    sbn.postTime,
                    userId,
                    moneyRepository)
                insertTrxHelper.detextApp()
            }
        }
        else{
            val ext = sbn.notification.extras

            val text = ext.getCharSequence("android.text").toString()
            val title = ext.getCharSequence("android.title").toString()

            scope.launch {
                val insertTrxHelper =  InsertTrxHelper(title,
                    text,
                    BRIMO_PKGNAME,
                    sbn.postTime,
                    userId,
                    moneyRepository)
                insertTrxHelper.detextApp()
            }

        }

        if (tmpt == text && tmptime <= sbn.postTime.toInt()){
            // fix double insert
            cancelNotification(sbn.key);
        }

    }

    private fun matchNotificationCode(sbn: StatusBarNotification): Int {

        return when(sbn.packageName){
            BRIMO_PKGNAME -> InterceptedNotificationCode.BRIMO_CODE
            OVO_PKGNAME -> InterceptedNotificationCode.OVO_CODE
            SMS_PKGNAME -> InterceptedNotificationCode.SMS_CODE
            DANA_PKGNAME -> InterceptedNotificationCode.DANA_CODE
            else -> InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE
        }
    }
}