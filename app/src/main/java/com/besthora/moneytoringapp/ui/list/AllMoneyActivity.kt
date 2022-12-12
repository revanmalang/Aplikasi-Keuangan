package com.besthora.moneytoringapp.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.databinding.ActivityAllMoneyBinding
import com.besthora.moneytoringapp.helper.Moveactivity
import com.besthora.moneytoringapp.ui.list.AdapterMoneyForAllMoney
import com.besthora.moneytoringapp.ui.list.AllMoneyViewModel
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("DEPRECATION")
class AllMoneyActivity : AppCompatActivity() {
    private var navbottom: MeowBottomNavigation? = null
    private lateinit var binding : ActivityAllMoneyBinding
    private var moveactv = Moveactivity()
    private val viewModel: AllMoneyViewModel by viewModel()
    private lateinit var adapterMoneyForAllMoney: AdapterMoneyForAllMoney
    private lateinit var chipGroup: ChipGroup
    private val preferencesMoney: PreferencesMoney by inject()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = (if(preferencesMoney.getmode != null) preferencesMoney.getmode else null).toString()

        navbottom = binding.meowbottom
        navbottom!!.add(MeowBottomNavigation.Model(1, R.drawable.ic_home2))
        navbottom!!.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_format_list_bulleted_24))
        navbottom!!.add(MeowBottomNavigation.Model(3, R.drawable.ic_add))
        navbottom!!.add(MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person_outline_24))
        navbottom!!.show(2)

        navbottom!!.setOnShowListener { item: MeowBottomNavigation.Model ->
            when(item.id){
                1 -> moveactv.To(this, "HomeActivity")
                3 -> moveactv.To(this, "CreateTrxActivity")
                4 -> moveactv.To(this, "ProfileActivity")
            }
        }

        adapterMoneyForAllMoney = AdapterMoneyForAllMoney()
        binding.listdataalltrx.adapter = adapterMoneyForAllMoney
        binding.listdataalltrx.layoutManager = LinearLayoutManager(this)
        binding.listdataalltrx.setHasFixedSize(true)

        showAllTrx()

        chipGroup = binding.chipsfilter
        chipGroup.setOnCheckedChangeListener { chipGroup, i ->
            val chip = chipGroup.findViewById<Chip>(i)
            if (chip != null) {
                when(chip.chipText){
                    "Pengeluaran" -> filterShowMoney("pengeluaran")
                    "Pemasukan" -> filterShowMoney("pemasukan")
                    "Transaksi Online" -> filterShowMoney("online")
                    "Transaksi Offline" -> filterShowMoney("offline")
                    "Custom" -> filterShowMoney("custom")
                }
            }else{
                showAllTrx()
            }
        }

    }
    private fun filterShowMoney(filter : String) {

        if (filter == "online" || filter == "offline"){
            lifecycleScope.launch {
                viewModel.getAllMoneyByWhentrx(filter,userId)
                    .collect {
                        adapterMoneyForAllMoney.submitList(it) {
                            if (binding.listdataalltrx.computeVerticalScrollOffset() == 0) {
                                binding.listdataalltrx.scrollToPosition(0)
                            }
                        }
                    }
            }
        }else if(filter == "pemasukan" || filter == "pengeluaran"){
            lifecycleScope.launch {
                viewModel.getAllMoneyBytipe(filter,userId)
                    .collect {
                        adapterMoneyForAllMoney.submitList(it) {
                            if (binding.listdataalltrx.computeVerticalScrollOffset() == 0) {
                                binding.listdataalltrx.scrollToPosition(0)
                            }
                        }
                    }
            }
        }else{
            showcustomFilter()
        }
    }

    private fun showAllTrx(){
        lifecycleScope.launch {
            viewModel.getAllMoneyForAny(userId)
                .collect { listtrx ->
                    adapterMoneyForAllMoney.submitList(listtrx) {
                        if (binding.listdataalltrx.computeVerticalScrollOffset() == 0) {
                            binding.listdataalltrx.scrollToPosition(0)
                        }
                    }
                }
        }
    }
    private fun showcustomFilter() {
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