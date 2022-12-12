package com.besthora.moneytoringapp.ui.create

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.besthora.moneytoringapp.R
import com.besthora.moneytoringapp.data.helper.InsertTrxHelper
import com.besthora.moneytoringapp.data.prefsingleton.PreferencesMoney
import com.besthora.moneytoringapp.data.repository.MoneyRepository
import com.besthora.moneytoringapp.databinding.ActivityCreateTrxBinding
import com.besthora.moneytoringapp.helper.Moveactivity
import com.besthora.moneytoringapp.service.TrxNotificationListenerService
import com.besthora.moneytoringapp.ui.auth.LoginLandingActivity
import com.besthora.moneytoringapp.ui.main.HomeActivity
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class CreateTrxActivity : AppCompatActivity() {
    private var navbottom: MeowBottomNavigation? = null
    private lateinit var binding : ActivityCreateTrxBinding
    private var moveactv = Moveactivity()
    private var type: Int = 1
    private var amounts: Double = 0.0
    private var dates: Long = 0
    private var invertedDate: Long = 0
    private lateinit var modetrx : AutoCompleteTextView
    private lateinit var chipGroup: ChipGroup
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
     var modesss : String = ""
    private var isSubmitted: Boolean = false
    private val preferencesMoney: PreferencesMoney by inject()
    private val moneyRepository : MoneyRepository by inject()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTrxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
        }
        auth = Firebase.auth


        modetrx = binding.inputmodetrx
        val arraymode = resources.getStringArray(R.array.trxmode)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, arraymode)
        modetrx.setAdapter(adapter)
        modetrx.setOnItemClickListener({ parent, view, position, id ->
            modesss = id.toString()
            if (id.toInt() == 0) {
                preferencesMoney.tmpinsertmode = "online"
            }else{
                preferencesMoney.tmpinsertmode = "offline"
            }
        })

        // set default tmp
        preferencesMoney.tmpinsertmode = null
        preferencesMoney.tmpinsertipe = null
        // set default tmp

        chipGroup = binding.chipstipe
        chipGroup.setOnCheckedChangeListener { chipGroup, i ->
            val chip = chipGroup.findViewById<Chip>(i)
            if (chip != null) {
                when(chip.chipText){
                    "Pengeluaran" -> {
                        preferencesMoney.tmpinsertipe = "pengeluaran"
                    }
                    "Pemasukan" -> {
                        preferencesMoney.tmpinsertipe = "pemasukan"
                    }
                }
            }
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
        dates = currentDate!!.time
        binding.date.setOnClickListener {
            clickDatePicker()
        }

        binding.saveButton.setOnClickListener {
            saveTransactionData()


//            if (!isSubmitted){
//                saveTransactionData()
//            }else{
//                Snackbar.make(findViewById(android.R.id.content), "Data transaksi berhasil disimpan", Snackbar.LENGTH_LONG).show()
//            }

        }

        navbottom = binding.meowbottom
        navbottom!!.add(MeowBottomNavigation.Model(1, R.drawable.ic_home2))
        navbottom!!.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_format_list_bulleted_24))
        navbottom!!.add(MeowBottomNavigation.Model(3, R.drawable.ic_add))
        navbottom!!.add(MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person_outline_24))
        navbottom!!.show(3)

        navbottom!!.setOnShowListener { item: MeowBottomNavigation.Model ->
            when(item.id){
                1 -> moveactv.To(this, "HomeActivity")
                2 -> moveactv.To(this, "AllMoneyActivity")
                4 -> moveactv.To(this, "ProfileActivity")
            }
        }
    }

    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                binding.date.text = null
                binding.date.hint = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                dates = theDate!!.time

            },
            year,
            month,
            day
        )
        dpd.show()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveTransactionData() {
        val amount = binding.amount.text.toString()
        val title = binding.title.text.toString()
        val note = binding.note.text.toString()

        if(amount.isEmpty()){
            binding.amount.error = "Mohon Masukkan Jumlah"
        }else if(title.isEmpty()) {
            binding.title.error = "Mohon Masukkan Judul"
        }else if (preferencesMoney.tmpinsertipe == null){
            Snackbar.make(findViewById(android.R.id.content), "Pilih dulu jenis transaksinya", Snackbar.LENGTH_LONG).show()
        }
        else{
            val idtrx: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(note.toByteArray()+ preferencesMoney.tmpinsertipe!!.toByteArray())
            } else {
                "null"
            }
            scope.launch {

                moneyRepository.insertTrx(
                    md5(idtrx+UUID.randomUUID()).toHex(),
                    "${preferencesMoney.getmode}",
                    "manual.insert.data",
                    "",
                    "${title}",
                    "${note}",
                    "${amount}",
                    "${dates}",
                    "${preferencesMoney.tmpinsertipe}",
                    "${preferencesMoney.tmpinsertmode}")
            }
            // clear fill
            binding.amount.text = null
            binding.title.text = null
            binding.date.text = null
            binding.date.hint = null
            binding.inputmodetrx.text = null
            binding.note.text = null
            // end

            Snackbar.make(findViewById(android.R.id.content), "Success create transaksi", Snackbar.LENGTH_LONG).show()
            Handler().postDelayed({
                moveactv.To(this, "AllMoneyActivity")
            }, 1000)
            isSubmitted = true
        }
    }
    fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(
        Charsets.UTF_8
    ))
    fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveactv.To(this, "${intent.getStringExtra("ACT_NAME")}")
    }
}