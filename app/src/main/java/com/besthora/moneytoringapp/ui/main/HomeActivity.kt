package com.besthora.moneytoringapp.ui.main

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.databinding.ActivityHomeBinding
import com.besthora.moneytoringapp.helper.Moveactivity
import com.besthora.moneytoringapp.ui.helper.IntToDecimal
import com.besthora.moneytoringapp.ui.list.AllMoneyViewModel
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HomeActivity : AppCompatActivity() {
    private var navbottom: MeowBottomNavigation? = null
    private lateinit var binding : ActivityHomeBinding
    private var moveactv = Moveactivity()
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    private val viewModel: AllMoneyViewModel by viewModel()
    private lateinit var adapterMoneyForHome: AdapterMoneyForHome
    private val preferencesMoney: PreferencesMoney by inject()

    private lateinit var userId : String
    private lateinit var statusindicator : String
    private  var pemasukan : String = ""
    private  var pengeluaran : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (preferencesMoney.getmode == null && intent.getStringExtra("MODE_GUEST_EXTRA") == "gueston"){
            preferencesMoney.getmode = "guest_${UUID.randomUUID()}"

        }
        userId =  preferencesMoney.getmode.toString()

        navbottom = binding.meowbottom
        navbottom!!.add(MeowBottomNavigation.Model(1, R.drawable.ic_home2))
        navbottom!!.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_format_list_bulleted_24))
        navbottom!!.add(MeowBottomNavigation.Model(3, R.drawable.ic_add))
        navbottom!!.add(MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person_outline_24))
        navbottom!!.show(1)

        navbottom!!.setOnShowListener { item: MeowBottomNavigation.Model ->
            when(item.id){
                2 -> moveactv.To(this, "AllMoneyActivity")
                3 -> moveactv.To(this, "CreateTrxActivity")
                4 -> moveactv.To(this, "ProfileActivity")
            }
        }

        binding.toAllTransaksi.setOnClickListener {
            moveactv.To(this, "AllMoneyActivity")
        }



        if (!isNotifServiceEnable()) {
            if (preferencesMoney.permissionnotif == "decline"){
            }else{
                dialogConfirmPermission()
            }
        }

        adapterMoneyForHome = AdapterMoneyForHome()
        binding.recylerHome.adapter = adapterMoneyForHome
        binding.recylerHome.layoutManager = LinearLayoutManager(this)
        binding.recylerHome.setHasFixedSize(true)

        getLatestTrx()

        getTotalPemasukanAndPengeluaran()

        setStatus()

    }

    private fun getLatestTrx() {
        lifecycleScope.launch {
            viewModel.getAllMoneyForHome(userId)
                .collect { listtrx ->
                    adapterMoneyForHome.submitList(listtrx) {
                        if (binding.recylerHome.computeVerticalScrollOffset() == 0) {
                            binding.recylerHome.scrollToPosition(0)
                        }
                    }
                    binding.shimmerHomeItem.visibility = View.GONE
                }
        }


    }

    private fun getTotalPemasukanAndPengeluaran() {
        lifecycleScope.launch {
            viewModel.getAllMoneyBytipe("pemasukan", userId)
                .collect { state ->
                    val total = state.map {
                        it.Nominal
                            .replace("Rp", "")
                            .replace(".","")
                            .split(",")[0]
                            .toInt() }.sum()
                    preferencesMoney.pemasukan = total.toString()
                    binding.showTotalPemasukan.text = "Rp${if (total<=0 || total == null) "0" else IntToDecimal.formatDecimalSeperators(total.toString())}"
                    binding.shimmerHomeNominalIn.visibility = View.GONE
                }
        }
        lifecycleScope.launch {
            viewModel.getAllMoneyBytipe("pengeluaran", userId)
                .collect { state ->
                    val total = state.map {
                        it.Nominal
                            .replace("Rp", "")
                            .replace(".","")
                            .split(",")[0]
                            .toInt() }.sum()
                    preferencesMoney.pengeluaran = total.toString()
                    binding.showTotalPengeluarans.text = "Rp${if (total<=0 || total == null) "0" else IntToDecimal.formatDecimalSeperators(total.toString())}"
                    binding.shimmerHomeNominalOut.visibility = View.GONE
                }
        }
    }

    private fun setStatus(){
        if ((preferencesMoney.pemasukan != null && preferencesMoney.pengeluaran != null)
            && (preferencesMoney.pemasukan!!.toInt() < preferencesMoney.pengeluaran!!.toInt())){
            binding.statusUser.text = "Boros"
        }else{
            binding.statusUser.text = "Hemat"
        }
    }

    private fun dialogConfirmPermission() {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.confirm_permission)


        val confirm: Chip = dialog.findViewById(R.id.chip_confim)
        val cancle: Chip = dialog.findViewById(R.id.chip_cancle)

        confirm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                preferencesMoney.permissionnotif = "accept"
                startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }

        })
        cancle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                preferencesMoney.permissionnotif = "decline"
                dialog.cancel()
            }
        })
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)


    }
    private fun isNotifServiceEnable(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        Log.d("flat", "isNotifServiceEnable: ${flat}")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0);
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
    }
}