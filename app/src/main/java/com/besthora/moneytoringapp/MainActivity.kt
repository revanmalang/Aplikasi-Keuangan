package com.besthora.moneytoringapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.besthora.moneytoringapp.service.TrxDataNotif

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.besthora.moneytoringapp")
        registerReceiver(TrxStateReceiver, intentFilter)
    }
    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.besthora.moneytoringapp")
        registerReceiver(TrxStateReceiver, intentFilter)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(TrxStateReceiver)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(TrxStateReceiver)
    }
    private val TrxStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val datanot = intent.getParcelableExtra<TrxDataNotif>("PARCEL_NOTIF")
            Log.d("notifparcelmain", "onReceivemain: ${datanot!!.NotifBody.toString()}")
        }
    }


}